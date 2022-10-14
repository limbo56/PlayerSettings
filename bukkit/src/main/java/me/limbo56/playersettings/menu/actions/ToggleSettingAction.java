package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.SettingsInventory;
import me.limbo56.playersettings.menu.renderers.SettingRenderer;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ToggleSettingAction extends SettingItemAction {
  public ToggleSettingAction(
      SettingRenderer renderer, SettingsInventory inventory, Setting setting) {
    super(renderer, inventory, setting);
  }

  @Override
  public void execute(ClickType type, SettingUser settingUser) {
    Player player = settingUser.getPlayer();
    int value = settingUser.getSettingWatcher().getValue(setting.getName());
    int newValue = value == 0 ? 1 : -value;
    player.performCommand("settings set " + setting.getName() + " " + newValue);
    super.execute(type, settingUser);
  }
}
