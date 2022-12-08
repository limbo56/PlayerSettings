package me.limbo56.playersettings.menu.renderers;

import com.cryptomorin.xseries.XItemStack;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.SettingsMenuHolder;
import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.menu.actions.AugmentSettingAction;
import me.limbo56.playersettings.menu.actions.ToggleSettingAction;
import me.limbo56.playersettings.util.ItemBuilder;
import me.limbo56.playersettings.util.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class MenuSettingsRenderer implements SettingsMenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  private static int getSettingOwnerValue(
      @NotNull SettingsMenuHolder menuHolder, @NotNull Setting setting) {
    return menuHolder.getOwner().getSettingWatcher().getValue(setting.getName());
  }

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

    // Render setting item
    MenuItem settingMenuItem = getSettingItem(menuHolder, setting);
    AugmentSettingAction settingAction = new AugmentSettingAction(this, menuHolder, setting);
    menuHolder.renderItem(new SettingsMenuItem(settingMenuItem, settingAction));

    // Render toggle item
    if (PlayerSettingsProvider.isToggleButtonEnabled()) {
      MenuItem toggleItem = getToggleItem(menuHolder, setting);
      ToggleSettingAction toggleSettingAction = new ToggleSettingAction(this, menuHolder, setting);
      menuHolder.renderItem(new SettingsMenuItem(toggleItem, toggleSettingAction));
    }
  }

  @NotNull
  private MenuItem getSettingItem(
      @NotNull SettingsMenuHolder menuHolder, @NotNull Setting setting) {
    SettingsMenuItem menuItem = menuHolder.getMenuItem(setting.getItem().getSlot());
    ItemStack itemStack =
        ItemBuilder.translateItemStack(
            menuItem == null ? setting.getItem().getItemStack().clone() : menuItem.getItemStack());
    int value = getSettingOwnerValue(menuHolder, setting);
    return ImmutableMenuItem.builder()
        .from(setting.getItem())
        .itemStack(formatSettingItem(itemStack, setting, value))
        .build();
  }

  @NotNull
  private ItemStack formatSettingItem(ItemStack itemStack, Setting setting, int value) {
    // Display setting value as item amount
    itemStack.setAmount(Math.max(value, 1));

    ItemMeta templateMeta = setting.getItem().getItemStack().getItemMeta();
    ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      if (templateMeta != null) {
        List<String> lore = formatSettingLore(setting, value, templateMeta.getLore());
        meta.setLore(lore);
      }

      // Add or remove glow
      if (value > 0) {
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
      } else {
        meta.removeEnchant(Enchantment.LURE);
      }

      itemStack.setItemMeta(meta);
    }
    return itemStack;
  }

  @NotNull
  private List<String> formatSettingLore(Setting setting, int value, Collection<String> lore) {
    int maxValue = setting.getMaxValue();
    int nextValue = getNextValue(maxValue, value);
    int previousValue = getPreviousValue(maxValue, value);
    return Text.from(lore)
        .placeholder("%current%", PlayerSettingsProvider.formatSettingValue(value))
        .placeholder("%max%", PlayerSettingsProvider.formatSettingValue(maxValue))
        .placeholder("%next%", PlayerSettingsProvider.formatSettingValue(nextValue))
        .placeholder("%previous%", PlayerSettingsProvider.formatSettingValue(previousValue))
        .build();
  }

  @NotNull
  private MenuItem getToggleItem(@NotNull SettingsMenuHolder menuHolder, @NotNull Setting setting) {
    String togglePath = getSettingOwnerValue(menuHolder, setting) > 0 ? "enabled" : "disabled";
    ConfigurationSection toggleItemSection =
        Preconditions.checkNotNull(
            PLUGIN.getItemsConfiguration().getFile().getConfigurationSection(togglePath),
            "Toggle item section is null");
    ItemStack toggleItem = XItemStack.deserialize(toggleItemSection);
    int page = setting.getItem().getPage();
    int slot = setting.getItem().getSlot() + 9;

    // Apply overrides
    ConfigurationSection overridesSection =
        PLUGIN.getSettingsConfiguration().getSettingOverridesSection(setting.getName());
    if (overridesSection != null) {
      ConfigurationSection toggleOverrides =
          overridesSection.getConfigurationSection(togglePath + "-item");
      if (toggleOverrides != null) {
        toggleItem = ItemBuilder.translateItemStack(toggleItem);
        slot = toggleOverrides.getInt("slot", slot);
      }
    }

    return ImmutableMenuItem.of(ItemBuilder.translateItemStack(toggleItem), page, slot);
  }

  private boolean isSettingWithingMenuBounds(SettingsMenuHolder menuHolder, Setting setting) {
    int offset = PlayerSettingsProvider.isToggleButtonEnabled() ? 9 : 0;
    int menuSize = menuHolder.getSize();
    int slot = setting.getItem().getSlot();
    return slot + offset < menuSize;
  }

  private int getPreviousValue(int maxValue, int value) {
    int previousValue = value - 1;
    boolean isToggledValue = value < 0;
    return isToggledValue ? -value : previousValue < 0 ? maxValue : previousValue;
  }

  private int getNextValue(int maxValue, int value) {
    int nextValue = value + 1;
    boolean isToggledValue = value < 0;
    return isToggledValue ? -value : nextValue > maxValue ? 0 : nextValue;
  }
}
