package me.limbo56.playersettings.menu.action;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class LevelSettingAction extends SettingItemAction {
  private final UserManager userManager;

  public LevelSettingAction(SettingsMenuItem.Context context, InternalSetting setting) {
    super(context, setting);
    this.userManager = PlayerSettings.getInstance().getUserManager();
  }

  @Override
  public void onExecute(SettingUser user, ClickType type) {
    Player player = user.getPlayer();
    int maxValue = getMaxValue(player, setting);
    int currentValue = user.getSettingWatcher().getValue(setting.getName());
    int newValue = calculateNewValue(type, maxValue, currentValue);
    userManager.getUser(player.getUniqueId()).changeSetting(setting.getName(), newValue);
  }

  private int calculateNewValue(ClickType type, int maxValue, int currentValue) {
    // Toggle setting
    if (currentValue < 0) return -currentValue;
    // Disable setting if over max value
    if (currentValue > maxValue) return 0;
    // Cycle to previous value or max value if value is 0
    if (type.isRightClick()) return currentValue == 0 ? maxValue : currentValue - 1;
    // Cycle to next value or 0 if value is max
    return currentValue == maxValue ? 0 : currentValue + 1;
  }

  private int getMaxValue(Player player, Setting setting) {
    int permissionLevel = Permissions.getSettingPermissionLevel(player, setting);
    return Math.max(1, Math.min(setting.getMaxValue(), permissionLevel));
  }
}
