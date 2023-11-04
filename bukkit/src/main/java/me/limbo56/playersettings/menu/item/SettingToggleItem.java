package me.limbo56.playersettings.menu.item;

import com.cryptomorin.xseries.XItemStack;
import com.google.common.base.Preconditions;
import java.util.function.Function;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.actions.ToggleSettingAction;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.renderers.SettingsRenderer;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class SettingToggleItem extends SettingsMenuItem {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public SettingToggleItem(SettingsRenderer renderer, Setting setting, MenuHolder menuHolder) {
    super(getMenuItem(setting, menuHolder), new ToggleSettingAction(renderer, menuHolder, setting));
  }

  private static MenuItem getMenuItem(Setting setting, MenuHolder menuHolder) {
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
      SettingItemFormatter.formatSettingItem(
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
