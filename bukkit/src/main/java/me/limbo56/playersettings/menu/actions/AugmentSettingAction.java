package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.menu.renderers.SettingsRenderer;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class AugmentSettingAction extends SettingItemAction {
  public AugmentSettingAction(SettingsRenderer renderer, MenuHolder inventory, Setting setting) {
    super(renderer, inventory, setting);
  }

  @Override
  public void execute(SettingsMenuItem menuItem, ClickType type, SettingUser settingsPlayer) {
    Player player = settingsPlayer.getPlayer();
    int maxValue = getMaxValue(player, setting);
    int currentValue = settingsPlayer.getSettingWatcher().getValue(setting.getName());
    int newValue = calculateNewValue(type, maxValue, currentValue);

    player.performCommand("settings set " + setting.getName() + " " + newValue);
    super.execute(menuItem, type, settingsPlayer);
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
