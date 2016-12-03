package me.lim_bo56.settings.listeners;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.config.MessageConfiguration;
import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.player.CustomPlayer;
import me.lim_bo56.settings.utils.Cache;
import me.lim_bo56.settings.utils.Updater;
import me.lim_bo56.settings.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Iterator;

/**
 * Created by lim_bo56
 * On 8/27/2016
 * At 12:00 AM
 */
@SuppressWarnings("unused")
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        CustomPlayer cPlayer = new CustomPlayer(player);

        if (!cPlayer.containsPlayer()) {
            cPlayer.addPlayer();
        }

        cPlayer.loadSettings();

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                CustomPlayer oPlayer = new CustomPlayer(online);

                if (!oPlayer.hasVisibility())
                    online.hidePlayer(player);

                if (oPlayer.hasVanish())
                    online.hidePlayer(player);

            }

            for (Player online : Bukkit.getOnlinePlayers()) {
                if (cPlayer.hasVisibility()) {
                    player.showPlayer(online);
                } else if (!cPlayer.hasVisibility()) {
                    player.hidePlayer(online);
                }
            }

            if (cPlayer.hasVanish()) {

                player.addPotionEffect(Cache.INVISIBILITY);

                for (Player online : Bukkit.getOnlinePlayers())
                    online.hidePlayer(player);

            } else {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }

            if (cPlayer.hasFly())
                player.setAllowFlight(true);

            if (cPlayer.hasSpeed())
                player.addPotionEffect(Cache.SPEED);
            else
                player.removePotionEffect(PotionEffectType.SPEED);

            if (cPlayer.hasJump())
                player.addPotionEffect(Cache.JUMP);
            else
                player.removePotionEffect(PotionEffectType.JUMP);


            if (Core.getInstance().getConfig().getBoolean("Update-Message"))
                if (player.isOp())
                    player.sendMessage(Updater.playerUpdater());
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CustomPlayer cPlayer = new CustomPlayer(player);

        cPlayer.saveSettings();

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

    }

    @EventHandler
    public void onGamemodeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        CustomPlayer cPlayer = new CustomPlayer(player);

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName()))
            if (event.getNewGameMode() == GameMode.ADVENTURE || event.getNewGameMode() == GameMode.SURVIVAL) {
                cPlayer.setFly(false);
            } else if (event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR) {
                cPlayer.setFly(true);
            }

    }

    @EventHandler
    public void onPlayersChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        CustomPlayer cPlayer = new CustomPlayer(player);

        Iterator<Player> recipients = event.getRecipients().iterator();

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

            if (!cPlayer.hasChat()) {
                event.getRecipients().remove(player);
                event.setCancelled(true);
                player.sendMessage(MessageConfiguration.get("Chat-Disabled"));
            } else if (cPlayer.hasChat()) {
                event.getRecipients().add(player);
                event.setCancelled(false);

                for (Player online : Bukkit.getOnlinePlayers()) {
                    CustomPlayer customPlayer = new CustomPlayer(online);

                    if (!customPlayer.hasChat())
                        event.getRecipients().remove(online);
                }

            }


        }


    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        Entity entity = event.getRightClicked();

        CustomPlayer cPlayer = new CustomPlayer(player);

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            if (entity != null)
                if (entity instanceof Player)
                    if (((Player) entity).getDisplayName() != null)
                        if (cPlayer.hasStacker()) {

                            CustomPlayer ePlayer = new CustomPlayer((Player) entity);

                            if (ePlayer.hasStacker()) {

                                player.setPassenger(entity);

                                if (Utilities.isVersion("v1_9_R1")) {
                                    Core.getInstance().getMount().sendMountPacket(player);
                                } else if (Utilities.isVersion("v1_9_R2")) {
                                    Core.getInstance().getMount().sendMountPacket(player);
                                } else if (Utilities.isVersion("v1_10_R1")) {
                                    Core.getInstance().getMount().sendMountPacket(player);
                                }

                            } else if (!ePlayer.hasStacker())
                                if (ConfigurationManager.getMessages().getBoolean("Send.Target-Stacker-Disabled"))
                                    if (!ConfigurationManager.getConfig().getBoolean("Using-Citizens")) {
                                        player.sendMessage(MessageConfiguration.get("Target-Stacker-Disabled"));
                                    }

                        } else if (!cPlayer.hasStacker())
                            if (ConfigurationManager.getMessages().getBoolean("Send.Player-Stacker-Disabled"))
                                if (!ConfigurationManager.getConfig().getBoolean("Using-Citizens")) {
                                    player.sendMessage(MessageConfiguration.get("Player-Stacker-Disabled"));
                                }
        }

    }

    @EventHandler
    public void Launch(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Entity entity = player.getPassenger();

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            if (event.getAction() == Action.LEFT_CLICK_AIR) {
                if (player.getPassenger() != null) {

                    entity.getVehicle().eject();

                    if (Utilities.isVersion("v1_9_R1")) {
                        Core.getInstance().getMount().sendMountPacket(player);
                    } else if (Utilities.isVersion("v1_9_R2")) {
                        Core.getInstance().getMount().sendMountPacket(player);
                    } else if (Utilities.isVersion("v1_10_R1")) {
                        Core.getInstance().getMount().sendMountPacket(player);
                    }

                    Vector direction = player.getLocation().getDirection();
                    entity.setVelocity(direction.multiply(ConfigurationManager.getDefault().getInt("Stacker.launch-force")));
                    entity.setFallDistance(-10000.0F);
                }
            }
        }

    }

    @EventHandler
    public void HitEntity(EntityDamageByEntityEvent event) {
        if (Cache.WORLDS_ALLOWED.contains(event.getDamager().getWorld().getName())) {
            if (event.getDamager().getType() == EntityType.PLAYER) {
                Player player = (Player) event.getDamager();
                CustomPlayer cPlayer = new CustomPlayer(player);

                if (cPlayer.hasStacker()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
