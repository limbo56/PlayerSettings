package me.limbo56.playersettings.settings;

import me.limbo56.playersettings.api.event.SettingUpdateEvent;
import me.limbo56.playersettings.api.setting.Setting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SettingUpdateTask extends BukkitRunnable {
  private final Setting setting;
  private final Player player;
  private final int previousValue;
  private final int newValue;

  public SettingUpdateTask(Setting setting, Player player, int previousValue, int newValue) {
    this.setting = setting;
    this.player = player;
    this.previousValue = previousValue;
    this.newValue = newValue;
  }

  @Override
  public void run() {
    Bukkit.getPluginManager()
        .callEvent(new SettingUpdateEvent(player, setting, previousValue, newValue));
    setting
        .getCallbacks()
        .forEach(callback -> callback.notifyChange(setting, player, Math.max(0, newValue)));
  }
}
