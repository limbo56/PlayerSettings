package me.limbo56.playersettings.listeners;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.limbo56.playersettings.settings.DefaultSettings.VANISH_SETTING;

public class VanishSettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    String vanishSettingName = VANISH_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingRegistered(vanishSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    for (SettingUser settingUser :
        PLUGIN.getUserManager().getUsersWithSettingValue(VANISH_SETTING.getName(), true)) {
      Player userPlayer = settingUser.getPlayer();
      player.hidePlayer(userPlayer);
    }
  }
}
