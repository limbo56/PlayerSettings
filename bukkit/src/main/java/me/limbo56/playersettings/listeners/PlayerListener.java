package me.limbo56.playersettings.listeners;

import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.util.PluginUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
  private final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    if (player.isOp()) {
      PluginUpdater.sendUpdateMessage(player);
    }

    // Load player
    plugin.getLogger().fine("Loading settings of player '" + player.getName() + "'");
    Bukkit.getScheduler()
        .runTaskAsynchronously(plugin, () -> plugin.getUserManager().loadUser(uuid));
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();

    // Unload player
    plugin.getLogger().fine("Saving settings of player '" + player.getName() + "'");
    Bukkit.getScheduler()
        .runTaskAsynchronously(plugin, () -> plugin.getUserManager().unloadUser(uuid));
  }
}
