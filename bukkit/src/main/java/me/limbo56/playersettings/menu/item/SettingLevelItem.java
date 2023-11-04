package me.limbo56.playersettings.menu.item;

import java.util.Objects;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.actions.AugmentSettingAction;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.renderers.SettingsRenderer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class SettingLevelItem extends SettingsMenuItem {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public SettingLevelItem(SettingsRenderer renderer, Setting setting, MenuHolder menuHolder) {
    super(
        getMenuItem(setting, menuHolder), new AugmentSettingAction(renderer, menuHolder, setting));
  }

  private static MenuItem getMenuItem(Setting setting, MenuHolder menuHolder) {
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
    SettingItemFormatter.formatSettingItem(itemStack, itemMeta, player, setting, value);

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
