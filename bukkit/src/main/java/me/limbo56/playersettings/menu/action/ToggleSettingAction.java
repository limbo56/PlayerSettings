package me.limbo56.playersettings.menu.action;

import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ToggleSettingAction extends SettingItemAction {
  public ToggleSettingAction(SettingsMenuItem.Context context, InternalSetting setting) {
    super(context, setting);
  }

  @Override
  public void onExecute(SettingUser user, ClickType type) {
    Player player = user.getPlayer();
    int value = user.getSettingWatcher().getValue(setting.getName());
    int newValue = value == 0 ? 1 : -value;
    player.performCommand("settings set " + setting.getName() + " " + newValue);
  }
}
