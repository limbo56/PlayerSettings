package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSettings.STACKER_SETTING;
import static me.limbo56.playersettings.settings.DefaultSettings.VANISH_SETTING;

import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VanishSettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    String vanishSettingName = VANISH_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingLoaded(vanishSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    for (SettingUser vanishedPlayer : getVanishedPlayers()) {
      player.hidePlayer(vanishedPlayer.getPlayer());
    }
  }

  private Collection<SettingUser> getVanishedPlayers() {
    return PLUGIN
        .getUserManager()
        .getUsersWithSettingValue(VANISH_SETTING.getName(), true);
  }
}
