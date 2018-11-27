package me.limbo56.playersettings.listeners;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.player.SPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerListener implements Listener {
    private PlayerSettings plugin;

    public PlayerListener(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        SPlayer toBeLoaded = new SPlayer(uuid, plugin.getSettingStore());

        // Add player to database & load settings
        try {
            plugin.getDatabaseManager().addPlayer(toBeLoaded);
            plugin.getDatabaseManager().loadPlayer(
                    toBeLoaded,
                    (sPlayer) -> plugin.getsPlayerStore().addToStore(sPlayer.getUuid(), sPlayer));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        SPlayer toBeSaved = plugin.getsPlayerStore().getStored().get(uuid);

        // Add player to database & load settings
        try {
            plugin.getDatabaseManager().savePlayer(
                    toBeSaved,
                    (sPlayer) -> plugin.getsPlayerStore().removeFromStore(sPlayer.getUuid()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
