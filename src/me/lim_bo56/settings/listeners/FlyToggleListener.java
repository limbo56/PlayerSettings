package me.lim_bo56.settings.listeners;

import me.lim_bo56.settings.player.CustomPlayer;
import me.lim_bo56.settings.utils.Cache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

/**
 * Created by lim_bo56
 * On 8/27/2016
 * At 12:02 AM
 */

// FIXME: 11/3/2016
@SuppressWarnings("unused")
public class FlyToggleListener implements Listener {

    @EventHandler
    public void flightToggleEvent(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = new CustomPlayer(player);

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName()))
            if (player.getAllowFlight() && player.hasPermission("settings.fly")) {
                if (!customPlayer.hasFly()) {
                    customPlayer.setFly(true);
                }
            } else if (!player.getAllowFlight()) {
                if (customPlayer.hasFly()) {
                    customPlayer.setFly(false);
                }
            }

    }
}
