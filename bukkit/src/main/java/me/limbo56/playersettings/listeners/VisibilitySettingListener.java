package me.limbo56.playersettings.listeners;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;

import static me.limbo56.playersettings.settings.DefaultSettings.VISIBILITY_SETTING;

public class VisibilitySettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getSettingsManager().isSettingRegistered(VISIBILITY_SETTING.getName())) {
      return;
    }

    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    getPlayersWithVisibilityDisabled()
        .forEach(settingOwner -> settingOwner.getPlayer().hidePlayer(player));
  }

  private Collection<SettingUser> getPlayersWithVisibilityDisabled() {
    return PLUGIN.getUserManager().getUsersWithSettingValue(VISIBILITY_SETTING.getName(), false);
  }
}
