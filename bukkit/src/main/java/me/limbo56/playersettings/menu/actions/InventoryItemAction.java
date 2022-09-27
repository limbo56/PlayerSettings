package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public interface InventoryItemAction {
  void execute(ClickType clickType, SettingUser user);
}
