package me.limbo56.playersettings.menu.item;

import java.util.Objects;
import java.util.function.Supplier;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.menu.action.LevelSettingAction;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class LevelSettingItem extends SettingsMenuItem {
  public LevelSettingItem(SettingsMenuItem.Context context, InternalSetting setting) {
    super(
        new LevelSettingItemSupplier(context.getMenu().getOwner(), setting),
        new LevelSettingAction(context, setting));
  }

  private static final class LevelSettingItemSupplier implements Supplier<MenuItem> {
    private final SettingUser user;
    private final InternalSetting setting;
    private final ItemsConfiguration itemsConfiguration;

    public LevelSettingItemSupplier(SettingUser user, InternalSetting setting) {
      this.user = user;
      this.setting = setting;
      this.itemsConfiguration =
          PlayerSettings.getInstance()
              .getConfigurationManager()
              .getConfiguration(ItemsConfiguration.class);
    }

    @Override
    public MenuItem get() {
      int settingValue = user.getSettingWatcher().getValue(setting.getName());
      MenuItem settingItem = getSettingItem(settingValue, setting);
      ItemStack itemStack = formatSettingItem(user.getPlayer(), settingValue, setting, settingItem);
      return ImmutableMenuItem.copyOf(settingItem).withItemStack(itemStack);
    }

    @NotNull
    private MenuItem getSettingItem(int value, InternalSetting setting) {
      ConfigurationSection section =
          Objects.requireNonNull(
              PlayerSettings.getInstance()
                  .getConfigurationManager()
                  .getConfiguration(ItemsConfiguration.class)
                  .getFile()
                  .getConfigurationSection(setting.getName()));
      return Parsers.SETTING_ITEM_PARSER.parse(section, value);
    }

    private ItemStack formatSettingItem(
        Player player, int value, InternalSetting setting, MenuItem item) {
      ItemStack itemStack = item.getItemStack();
      format(player, value, setting, item);

      // Add item glow
      if (itemsConfiguration.isGlowEnabled(setting)) {
        addGlow(itemStack, value);
      }

      // Make item amount same as level
      itemStack.setAmount(Math.max(value, 1));

      return itemStack;
    }

    private void addGlow(ItemStack itemStack, int value) {
      ItemMeta meta = itemStack.getItemMeta();
      if (meta == null) {
        return;
      }

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
