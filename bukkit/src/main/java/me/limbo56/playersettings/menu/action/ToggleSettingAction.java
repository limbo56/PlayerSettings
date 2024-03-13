package me.limbo56.playersettings.menu.action;

import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import org.bukkit.event.inventory.ClickType;

public class ToggleSettingAction extends SettingItemAction {
  private final UserManager userManager;

  public ToggleSettingAction(SettingsMenuItem.Context context, InternalSetting setting) {
    super(context, setting);
    this.userManager = PlayerSettings.getInstance().getUserManager();
  }

  @Override
  public void onExecute(SettingUser user, ClickType type) {
    UUID uuid = user.getPlayer().getUniqueId();
    int value = user.getSettingWatcher().getValue(setting.getName());
    int newValue = value == 0 ? 1 : -value;
    userManager.getUser(uuid).changeSetting(setting.getName(), newValue);
  }
}
