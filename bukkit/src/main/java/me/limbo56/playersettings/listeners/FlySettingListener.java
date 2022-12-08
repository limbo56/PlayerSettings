package me.limbo56.playersettings.listeners;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import static me.limbo56.playersettings.settings.DefaultSettings.FLY_SETTING;

public class FlySettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
    String flySettingName = FLY_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingLoaded(flySettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(flySettingName)) {
      return;
    }

    SettingWatcher settingWatcher = user.getSettingWatcher();
    int flySetting = settingWatcher.getValue(flySettingName);
    if (!user.hasSettingEnabled(flySettingName) || !event.isFlying()) {
      return;
    }

    settingWatcher.setValue(flySettingName, flySetting, false);
  }

  @EventHandler
  public void onGameModeChange(PlayerGameModeChangeEvent event) {
    String flySettingName = FLY_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingLoaded(flySettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(FLY_SETTING.getName())) {
      return;
    }

    player.setAllowFlight(true);
  }
}
