package me.limbo56.playersettings.menu.action;

import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public interface MenuItemAction {

  void execute(SettingUser user, SettingsMenuItem menuItem, ClickType clickType);
}
