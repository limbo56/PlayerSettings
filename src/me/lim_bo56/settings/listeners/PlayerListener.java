package me.lim_bo56.settings.listeners;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.objects.CustomPlayer;
import me.lim_bo56.settings.utils.ColorUtils;
import me.lim_bo56.settings.utils.Updater;
import me.lim_bo56.settings.utils.Utilities;
import me.lim_bo56.settings.utils.Variables;
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

/**
 * Created by lim_bo56
 * On 8/27/2016
 * At 12:00 AM
 */
public class PlayerListener implements Listener {

    private ConfigurationManager messages = ConfigurationManager.getMessages();
    private ConfigurationManager menu = ConfigurationManager.getMenu();

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        CustomPlayer cPlayer = new CustomPlayer(player);

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                CustomPlayer oPlayer = new CustomPlayer(online);

                if (!oPlayer.hasVisibility()) {
                    player.hidePlayer(online);
                }

                if (oPlayer.hasVanish()) {
                    online.hidePlayer(player);
                }

            }

            if (!cPlayer.containsPlayer()) {
                cPlayer.addPlayer();
            }

            if (cPlayer.hasVisibility())
                for (Player online : Bukkit.getOnlinePlayers())
                    player.showPlayer(online);
            else if (!cPlayer.hasVisibility())
                for (Player online : Bukkit.getOnlinePlayers())
                    player.hidePlayer(online);

            if (cPlayer.hasVanish()) {

                player.addPotionEffect(Variables.INVISIBILITY);

                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.hidePlayer(player);
                }

            }

            if (cPlayer.hasFly()) {
                player.setAllowFlight(true);
            }

            if (cPlayer.hasSpeed()) {
                player.addPotionEffect(Variables.SPEED);
            }

            if (cPlayer.hasJump()) {
                player.addPotionEffect(Variables.JUMP);
            }


            if (player.isOp()) {
                player.sendMessage(ColorUtils.Color(Variables.CHAT_TITLE + Updater.playerUpdater()));
            }
        }

    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

    }

    @EventHandler
    public void onGamemodeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        CustomPlayer cPlayer = new CustomPlayer(player);

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName()))
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

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {

            if (!cPlayer.hasChat()) {
                event.getRecipients().remove(player);
                event.setCancelled(true);
                player.sendMessage(ColorUtils.Color(String.valueOf(messages.get("Chat-Disabled"))));
            } else if (cPlayer.hasChat()) {
                event.getRecipients().add(player);
                event.setCancelled(false);
            }

            for (Player recipients : event.getRecipients()) {

                CustomPlayer rPlayer = new CustomPlayer(recipients);

                if (!rPlayer.hasChat()) {
                    event.getRecipients().remove(recipients);
                }
            }

        }


    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        Entity entity = event.getRightClicked();

        CustomPlayer cPlayer = new CustomPlayer(player);
        CustomPlayer ePlayer = new CustomPlayer((Player) entity);

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {
            if (entity != null)
                if (entity instanceof Player)
                    if (((Player) entity).getDisplayName() != null)

                        if (cPlayer.hasStacker()) {

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
                                if (messages.getBoolean("Send.Target-Stacker-Disabled"))
                                    player.sendMessage(ColorUtils.Color(String.valueOf(messages.get("Target-Stacker-Disabled"))));

                        } else if (!cPlayer.hasStacker())
                            if (messages.getBoolean("Send.Player-Stacker-Disabled"))
                                player.sendMessage(ColorUtils.Color(String.valueOf(messages.get("Player-Stacker-Disabled"))));
        }

    }

    @EventHandler
    public void Launch(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Entity entity = player.getPassenger();

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {
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
                    entity.setVelocity(direction.multiply(1));
                    entity.setFallDistance(-10000.0F);
                }
            }
        }

    }

    @EventHandler
    public void HitEntity(EntityDamageByEntityEvent event) {
        if (menu.getStringList("worlds-allowed").contains(event.getDamager().getWorld().getName())) {
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
