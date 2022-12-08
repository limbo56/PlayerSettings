package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public interface MenuItemAction {

  void execute(SettingsMenuItem menuItem, ClickType clickType, SettingUser user);
}
