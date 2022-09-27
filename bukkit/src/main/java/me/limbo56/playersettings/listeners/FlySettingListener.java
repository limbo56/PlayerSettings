package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSetting.FLY_SETTING;

import java.util.Optional;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlySettingListener implements Listener {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
    Optional<SettingUser> optionalUser = getWatcherIfAccessible(event.getPlayer());
    if (!optionalUser.isPresent()) {
      return;
    }

    SettingUser user = optionalUser.get();
    String flySettingName = FLY_SETTING.getSetting().getName();
    if (!user.hasSettingEnabled(flySettingName)) {
      return;
    }

    SettingWatcher settingWatcher = user.getSettingWatcher();
    int flySetting = settingWatcher.getValue(flySettingName);
    if (!event.isFlying()) {
      return;
    }

    settingWatcher.setValue(flySettingName, Math.max(1, -flySetting), false);
  }

  @EventHandler
  public void onGameModeChange(PlayerGameModeChangeEvent event) {
    Player player = event.getPlayer();
    Optional<SettingUser> optionalUser = getWatcherIfAccessible(player);
    String flySettingName = FLY_SETTING.getSetting().getName();
    if (!optionalUser.isPresent() || !optionalUser.get().hasSettingEnabled(flySettingName)) {
      return;
    }

    player.setAllowFlight(true);
  }

  private Optional<SettingUser> getWatcherIfAccessible(Player player) {
    String worldName = player.getWorld().getName();
    if (!PlayerSettingsProvider.isAllowedWorld(worldName)) {
      return Optional.empty();
    }

    SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
    String flySettingName = FLY_SETTING.getSetting().getName();
    if (!user.hasSettingPermissions(flySettingName)) {
      return Optional.empty();
    }

    return Optional.of(user);
  }
}
