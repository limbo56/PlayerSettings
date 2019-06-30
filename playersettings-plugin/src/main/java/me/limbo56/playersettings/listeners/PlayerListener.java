package me.limbo56.playersettings.listeners;

import lombok.AllArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.settings.SimpleSettingWatcher;
import me.limbo56.playersettings.utils.PluginUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@AllArgsConstructor
public class PlayerListener implements Listener {
    private PlayerSettings plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        SettingWatcher settingWatcher = new SimpleSettingWatcher(
                event.getPlayer(),
                plugin.getSettingsRegistry().getStored(),
                plugin.getSettingsRegistry().getCallbacks().getStored()
        );
        SPlayer sPlayer = new SPlayer(uuid, settingWatcher);

        if (event.getPlayer().isOp()) PluginUpdater.sendUpdateMessage(event.getPlayer());

        // Load player
        sPlayer.loadPlayer();
        plugin.getSPlayerStore().addToStore(uuid, sPlayer);
        plugin.debug("Loaded player " + event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        SPlayer sPlayer = plugin.getSPlayer(uuid);

        sPlayer.unloadPlayer();
        plugin.getSPlayerStore().removeFromStore(uuid);
        plugin.debug("Saved player " + event.getPlayer().getName());
    }
}
