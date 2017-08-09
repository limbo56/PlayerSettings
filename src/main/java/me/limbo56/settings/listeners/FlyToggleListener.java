package me.limbo56.settings.listeners;

import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.*;
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

        CustomPlayer customPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

        if (!ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled")) {
            return;
        }

        if (!Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            return;
        }

        if (!customPlayer.getFly()
                && customPlayer.getDoubleJump()
                && player.hasPermission(Cache.DOUBLEJUMP_PERMISSION)
                && customPlayer.doubleJumpStatus) {
            event.setCancelled(true);

            player.setAllowFlight(false);
            player.setFlying(false);

            customPlayer.doubleJumpStatus = false;

            player.setVelocity(player.getLocation().getDirection().multiply(ConfigurationManager.getDefault().getDouble("DoubleJump.velocity.forward")).setY(ConfigurationManager.getDefault().getDouble("DoubleJump.velocity.up")));
            player.setFallDistance(-10000.0F);

            String sound = ConfigurationManager.getDefault().getString("DoubleJump.sound");

            if (sound == null || sound.isEmpty())
                return;

            try {
                if (sound.contains(":"))
                    player.playSound(player.getLocation(), SoundUtils.getEnumSound(sound.split(":")[0]).resolveSound(), 1F, Float.valueOf(sound.split(":")[1]));
                else
                    player.playSound(player.getLocation(), SoundUtils.valueOf(sound).resolveSound(), 1F, 0F);

            } catch (IllegalArgumentException exception) {
                if (player.isOp()) {
                    player.sendMessage(ColorUtils.Color(
                            Cache.CHAT_TITLE + "&4The sound specified for double jump in the configuration doesn't exist. " +
                                    "&cPlease check default.yml and check for errors in spelling. " +
                                    "Be aware that the sound names changed in 1.9 and up and are not the same as older versions."));
                }
            }
        }

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled")) {
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (!customPlayer.getDoubleJump() && player.getAllowFlight() && player.hasPermission(Cache.FLY_PERMISSION)) {
                    if (!customPlayer.getFly()) {
                        customPlayer.setFly(true);
                    }
                } else if (!player.getAllowFlight()) {
                    if (customPlayer.getFly()) {
                        customPlayer.setFly(false);
                    }
                }
            }
        }
    }
}
