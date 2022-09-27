package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.PlayerSettingsProvider.getPlugin;
import static me.limbo56.playersettings.settings.DefaultSetting.VANISH_SETTING;

import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VanishSettingListener implements Listener {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    getVanishedPlayers().forEach(vanished -> player.hidePlayer(getPlugin(), vanished));
  }

  private List<Player> getVanishedPlayers() {
    String vanishSettingName = VANISH_SETTING.getSetting().getName();
    return plugin.getUserManager().getUsers().stream()
        .filter(user -> user.hasSettingEnabled(vanishSettingName))
        .map(SettingUser::getPlayer)
        .collect(java.util.stream.Collectors.toList());
  }
}
