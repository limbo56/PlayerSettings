package me.limbo56.playersettings.menu.renderers;

import com.cryptomorin.xseries.XItemStack;
import com.google.common.base.Preconditions;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.menu.SettingsMenuHolder;
import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.menu.actions.AugmentSettingAction;
import me.limbo56.playersettings.menu.actions.ToggleSettingAction;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class MenuSettingsRenderer implements SettingsMenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull SettingsMenuHolder menuHolder, int page) {
    Collection<Setting> settings = PLUGIN.getSettingsManager().getSettingMap().values();
    settings.stream()
        .filter(setting -> setting.getItem().getPage() == page)
        .forEach(setting -> renderSetting(menuHolder, setting));
  }

  public void renderSetting(@NotNull SettingsMenuHolder menuHolder, @NotNull Setting setting) {
    String settingName = setting.getName();
    if (!isSettingWithingMenuBounds(menuHolder, setting)) {
      String settingWarning =
          "Setting '"
              + settingName
              + "' is not between the bounds of the menu (0-"
              + menuHolder.getSize()
              + ")";
      PLUGIN.getLogger().warning(settingWarning);
      return;
    }

    menuHolder.renderItem(new SettingLevelItem(this, setting, menuHolder));
    if (PLUGIN.getPluginConfiguration().isToggleButtonEnabled()) {
      menuHolder.renderItem(new SettingToggleItem(this, setting, menuHolder));
    }
  }

  private boolean isSettingWithingMenuBounds(SettingsMenuHolder menuHolder, Setting setting) {
    int offset = PLUGIN.getPluginConfiguration().isToggleButtonEnabled() ? 9 : 0;
    int menuSize = menuHolder.getSize();
    int slot = setting.getItem().getSlot();
    return slot + offset < menuSize;
  }

  private static final class SettingFormatter {
    private static void formatSettingItem(
        ItemStack itemStack, ItemMeta templateMeta, Player player, Setting setting, int value) {
      ItemMeta meta = itemStack.getItemMeta();
      if (meta != null) {
        formatSettingItemMeta(meta, templateMeta, player, setting, value);
        itemStack.setItemMeta(meta);
      }
    }

    private static void formatSettingItemMeta(
        ItemMeta meta, ItemMeta templateMeta, Player player, Setting setting, int value) {
      Text displayName = Text.from(templateMeta.getDisplayName());
      Text lore = Text.from(templateMeta.getLore());
      meta.setDisplayName(applySettingPlaceholders(displayName, player, setting, value).first());
      meta.setLore(applySettingPlaceholders(lore, player, setting, value).build());
    }

    @NotNull
    private static Text applySettingPlaceholders(
        Text meta, Player player, Setting setting, int value) {
      int maxValue = setting.getMaxValue();
      int nextValue = getNextValue(maxValue, value);
      int previousValue = getPreviousValue(maxValue, value);
      SettingsConfiguration settingsConfiguration = PLUGIN.getSettingsConfiguration();
      return meta.usePlaceholder(
              "%current%", settingsConfiguration.formatSettingValue(setting, value))
          .usePlaceholder("%max%", settingsConfiguration.formatSettingValue(setting, maxValue))
          .usePlaceholder("%next%", settingsConfiguration.formatSettingValue(setting, nextValue))
          .usePlaceholder(
              "%previous%", settingsConfiguration.formatSettingValue(setting, previousValue))
          .usePlaceholderApi(player);
    }

    private static int getNextValue(int value, int maxValue) {
      int nextValue = value + 1;
      boolean isToggledValue = value < 0;
      return isToggledValue ? -value : nextValue > maxValue ? 0 : nextValue;
    }

    private static int getPreviousValue(int value, int maxValue) {
      int previousValue = value - 1;
      boolean isToggledValue = value < 0;
      return isToggledValue ? -value : previousValue < 0 ? maxValue : previousValue;
    }
  }

  private static final class SettingLevelItem extends SettingsMenuItem {
    public SettingLevelItem(
        MenuSettingsRenderer renderer, Setting setting, SettingsMenuHolder menuHolder) {
      super(
          getMenuItem(setting, menuHolder),
          new AugmentSettingAction(renderer, menuHolder, setting));
    }

    private static MenuItem getMenuItem(Setting setting, SettingsMenuHolder menuHolder) {
      int settingValue = menuHolder.getOwner().getSettingWatcher().getValue(setting.getName());
      MenuItem settingItem = getSettingItem(setting, settingValue);
      ItemStack itemStack =
          formatSettingItem(settingItem, menuHolder.getOwner().getPlayer(), setting, settingValue);
      return ImmutableMenuItem.copyOf(settingItem).withItemStack(itemStack);
    }

    @NotNull
    private static MenuItem getSettingItem(Setting setting, int value) {
      ConfigurationSection section =
          Objects.requireNonNull(
              PLUGIN.getItemsConfiguration().getFile().getConfigurationSection(setting.getName()));
      return SettingsMenuItem.deserialize(section, value);
    }

    private static ItemStack formatSettingItem(
        MenuItem settingItem, Player player, Setting setting, int value) {
      ItemStack itemStack = settingItem.getItemStack();
      ItemMeta itemMeta = setting.getItem().getItemStack().getItemMeta();
      SettingFormatter.formatSettingItem(itemStack, itemMeta, player, setting, value);

      // Add item glow
      if (PLUGIN.getItemsConfiguration().isGlowEnabled(setting)) {
        addGlow(itemStack, value);
      }

      // Make item amount same as level
      itemStack.setAmount(Math.max(value, 1));

      return itemStack;
    }

    private static void addGlow(ItemStack itemStack, int value) {
      ItemMeta meta = itemStack.getItemMeta();
      if (meta != null) {
        if (value > 0) {
          meta.addEnchant(Enchantment.LURE, 1, false);
          meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
          meta.removeEnchant(Enchantment.LURE);
        }
        itemStack.setItemMeta(meta);
      }
    }
  }

  private static final class SettingToggleItem extends SettingsMenuItem {
    public SettingToggleItem(
        MenuSettingsRenderer renderer, Setting setting, SettingsMenuHolder menuHolder) {
      super(
          getMenuItem(setting, menuHolder), new ToggleSettingAction(renderer, menuHolder, setting));
    }

    private static MenuItem getMenuItem(Setting setting, SettingsMenuHolder menuHolder) {
      SettingUser user = menuHolder.getOwner();
      int settingValue = user.getSettingWatcher().getValue(setting.getName());
      String togglePath = settingValue > 0 ? "enabled" : "disabled";
      ItemStack itemStack = getToggleItem(togglePath, settingValue);
      ItemMeta templateMeta = itemStack.getItemMeta();
      int page = setting.getItem().getPage();
      int slot = setting.getItem().getSlot() + 9;

      // Apply overrides
      ConfigurationSection overridesSection =
          PLUGIN.getSettingsConfiguration().getSettingOverridesSection(setting.getName());
      if (overridesSection != null) {
        ConfigurationSection toggleOverrides =
            overridesSection.getConfigurationSection(togglePath + "-item");
        if (toggleOverrides != null) {
          itemStack = XItemStack.edit(itemStack, toggleOverrides, Function.identity(), null);
          slot = toggleOverrides.getInt("slot", slot);
        }
      }

      // Apply meta formatting
      if (templateMeta != null) {
        SettingFormatter.formatSettingItem(
            itemStack, templateMeta, user.getPlayer(), setting, settingValue);
      }
      return ImmutableMenuItem.of(itemStack, page, slot);
    }

    @NotNull
    private static ItemStack getToggleItem(String togglePath, int value) {
      ConfigurationSection toggleItemSection =
          Preconditions.checkNotNull(
              PLUGIN.getItemsConfiguration().getFile().getConfigurationSection(togglePath),
              "Toggle item section is null");
      return SettingsMenuItem.deserialize(toggleItemSection, value).getItemStack();
    }
  }
}
