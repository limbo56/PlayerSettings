package me.limbo56.playersettings.setting.callback;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FlySettingCallback implements SettingCallback {
  @Override
  public void notifyChange(Player player, Setting setting, int value) {
    if (!hasFlightGameMode(player)) {
      boolean hasFlyEnabled = value > 0;
      player.setAllowFlight(hasFlyEnabled);
    }

    float flightSpeed = Math.max(1, value) * 0.1F;
    player.setFlySpeed(flightSpeed);
  }

  @Override
  public void clear(Player player) {
    player.setAllowFlight(hasFlightGameMode(player));
    player.setFlySpeed(0.1F);
  }

  private boolean hasFlightGameMode(Player player) {
    GameMode gameMode = player.getGameMode();
    return gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR;
  }
}
