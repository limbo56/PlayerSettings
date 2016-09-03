package me.lim_bo56.settings.listeners;

import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.objects.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

/**
 * Created by lim_bo56
 * On 8/27/2016
 * At 12:02 AM
 */
public class FlyToggleListener implements Listener {

    private ConfigurationManager menu = ConfigurationManager.getMenu();

    @EventHandler
    public void flightToggleEvent(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = new CustomPlayer(player);

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName()))
            if (player.getAllowFlight()) {
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
