package me.limbo56.settings.listeners;

import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.Utilities;

import org.bukkit.GameMode;
import org.bukkit.Sound;
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

    @EventHandler
    public void flightToggleEvent(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        CustomPlayer customPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled")) {
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (!customPlayer.hasFly() && player.getGameMode() != GameMode.CREATIVE && player.hasPermission("settings.doublejump")) {
                    if (customPlayer.hasDoubleJump() && customPlayer.doubleJumpStatus) {
                        event.setCancelled(true);
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        customPlayer.doubleJumpStatus = false;
                        player.setVelocity(player.getLocation().getDirection().multiply(1.6D).setY(1.0D));
                        player.setFallDistance(-10000.0F);
                        if (ConfigurationManager.getDefault().getString("DoubleJump.sound") == null || ConfigurationManager.getDefault().getString("DoubleJump.sound").isEmpty())
                            return;
                        player.playSound(player.getLocation(), Sound.valueOf(ConfigurationManager.getDefault().getString("DoubleJump.sound")), 1.0F, 0.0F);
                    }
                }
            }
        }

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled")) {
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (!customPlayer.hasDoubleJump() && player.getAllowFlight() && player.hasPermission("settings.fly")) {
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
    }

}
