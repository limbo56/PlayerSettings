package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSettings.FLY_SETTING;

import java.util.Optional;
import java.util.function.Consumer;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.jetbrains.annotations.NotNull;

public class FlySettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private static final String FLIGHT_STATE_KEY = "flight-state";

  private static boolean isSaveFlightStateEnabled() {
    return PLUGIN
        .getSettingsConfiguration()
        .getFile()
        .getBoolean(FLY_SETTING.getName() + ".save-state", true);
  }

  private static boolean isForceFlightEnabled() {
    return PLUGIN
        .getSettingsConfiguration()
        .getFile()
        .getBoolean(FLY_SETTING.getName() + ".force-on-join", false);
  }

  @EventHandler
  public void onToggleFlight(PlayerToggleFlightEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    String flySettingName = FLY_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingRegistered(flySettingName)) {
      return;
    }

    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(FLY_SETTING.getName())) {
      return;
    }
    user.setFlying(event.isFlying());

    SettingWatcher settingWatcher = user.getSettingWatcher();
    int flySettingValue = settingWatcher.getValue(flySettingName);
    if (user.hasSettingEnabled(flySettingName) || !event.isFlying()) {
      return;
    }

    settingWatcher.setValue(flySettingName, flySettingValue, false);
  }

  @EventHandler
  public void onGameModeChange(PlayerGameModeChangeEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    String flySettingName = FLY_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingRegistered(flySettingName)) {
      return;
    }

    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(FLY_SETTING.getName())) {
      return;
    }

    player.setAllowFlight(true);
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    String flySettingName = FLY_SETTING.getName();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())
        || !PLUGIN.getSettingsManager().isSettingRegistered(flySettingName)
        || !isSaveFlightStateEnabled()) {
      return;
    }

    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(FLY_SETTING.getName())
        || !user.hasSettingEnabled(flySettingName)) {
      return;
    }

    Setting setting = PLUGIN.getSettingsManager().getSetting(flySettingName);
    new TaskChain()
        .async(
            data -> {
              String flyValue = String.valueOf(user.isFlying() ? 1 : 0);
              PLUGIN
                  .getSettingsDatabase()
                  .putExtra(player.getUniqueId(), setting, FLIGHT_STATE_KEY, flyValue);
            })
        .runAsync();
  }

  public static final class FlightStateLoader implements Consumer<SettingUser> {
    @Override
    public void accept(SettingUser user) {
      String flySettingName = FLY_SETTING.getName();
      Player player = user.getPlayer();
      if (!PLUGIN.getSettingsManager().isSettingRegistered(flySettingName)
          || player == null
          || !PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())
          || !user.hasSettingPermissions(FLY_SETTING.getName())
          || !user.hasSettingEnabled(flySettingName)) {
        return;
      }

      // Load saved flight state if enabled in the configuration
      if (isSaveFlightStateEnabled()) {
        Setting setting = PLUGIN.getSettingsManager().getSetting(flySettingName);
        new TaskChain()
            .loadAsync(FLIGHT_STATE_KEY, () -> getFlightState(player, setting))
            .sync(
                data -> {
                  int flightState = (int) data.get(FLIGHT_STATE_KEY);
                  if (player.isOnline() && flightState == 1) {
                    player.setFlying(true);
                    user.setFlying(true);
                  }
                })
            .runAsync();
        return;
      }

      // Force flight mode if specified in the fly setting configuration
      if (isForceFlightEnabled()) {
        player.setFlying(true);
        user.setFlying(true);
      }
    }

    @NotNull
    private Integer getFlightState(Player player, Setting setting) {
      return Optional.ofNullable(
              PLUGIN
                  .getSettingsDatabase()
                  .getExtra(player.getUniqueId(), setting, FLIGHT_STATE_KEY))
          .map(Integer::parseInt)
          .orElse(0);
    }
  }
}
