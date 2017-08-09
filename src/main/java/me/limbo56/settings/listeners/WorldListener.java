package me.limbo56.settings.listeners;

import com.statiocraft.jukebox.scJukeBox;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.PlayerUtils;
import me.limbo56.settings.utils.Utilities;
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

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

        if (!Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                CustomPlayer oPlayer = PlayerUtils.getOrCreateCustomPlayer(online);

                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled"))
                    if (oPlayer.getVanish()) {
                        online.hidePlayer(player);
                    } else {
                        player.showPlayer(online);
                    }
            }

            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);

            if (Utilities.hasRadioPlugin()) {
                if (scJukeBox.getCurrentJukebox(player) != null)
                    scJukeBox.getCurrentJukebox(player).removePlayer(player);
            }

            if (cPlayer.getDoubleJump() || cPlayer.getFly())
                player.setAllowFlight(false);

        }
    }
}
