package me.lim_bo56.settings.listeners;

import me.lim_bo56.settings.player.CustomPlayer;
import me.lim_bo56.settings.utils.Variables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:26:48 AM
 */
public class WorldListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (!Variables.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                CustomPlayer oPlayer = new CustomPlayer(online);

                if (oPlayer.hasVanish()) {
                    online.hidePlayer(player);
                } else {
                    player.showPlayer(online);
                }

            }

            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);

        }


    }

}
