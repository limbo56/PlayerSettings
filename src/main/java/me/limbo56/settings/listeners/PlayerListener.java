package me.limbo56.settings.listeners;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.config.MessageConfiguration;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.PlayerUtils;
import me.limbo56.settings.utils.ReflectionUtils;
import me.limbo56.settings.utils.Utilities;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

/**
 * Created by lim_bo56
 * On 8/27/2016
 * At 12:00 AM
 */

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        PlayerUtils.loadSettings(player);
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
                cPlayer.setFly(ConfigurationManager.getDefault().getBoolean("Default.Fly"));

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled"))
                cPlayer.setDoubleJump(ConfigurationManager.getDefault().getBoolean("Default.DoubleJump"));
        }

        PlayerUtils.saveSettings(player);
    }

    @EventHandler
    public void onGamemodeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            if (event.getNewGameMode() == GameMode.ADVENTURE || event.getNewGameMode() == GameMode.SURVIVAL) {
                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
                    cPlayer.setFly(ConfigurationManager.getDefault().getBoolean("Default.Fly"));

                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled"))
                    cPlayer.setDoubleJump(ConfigurationManager.getDefault().getBoolean("Default.DoubleJump"));
            } else if (event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR) {
                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
                    cPlayer.setFly(true);

                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled"))
                    cPlayer.setDoubleJump(false);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayersChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Chat.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (!cPlayer.getChat()) {
                    event.getRecipients().remove(player);
                    event.setCancelled(true);
                    player.sendMessage(MessageConfiguration.get("Chat-Disabled"));
                } else if (cPlayer.getChat()) {
                    event.getRecipients().add(player);

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        CustomPlayer customPlayer = PlayerUtils.getOrCreateCustomPlayer(online);

                        if (!customPlayer.getChat())
                            event.getRecipients().remove(online);
                    }
                }
            }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled")) {
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (cPlayer.getFly())
                    return;
                if (cPlayer.getDoubleJump()) {
                    if (!player.getLocation().subtract(0D, 1D, 0D).getBlock().isEmpty()) {
                        player.setAllowFlight(true);
                        cPlayer.doubleJumpStatus = true;
                    }
                } else
                    player.setAllowFlight(false);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        Entity entity = event.getRightClicked();

        boolean isNPC = entity.hasMetadata("NPC");

        if (Utilities.hasAuthMePlugin() && entity instanceof Player && !Utilities.isAuthenticated((Player) entity))
            return;

        CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

        if (!(Utilities.isVersion("v1_8_R1") || Utilities.isVersion("v1_8_R2") || Utilities.isVersion("v1_8_R3")))
            if (event.getHand() == EquipmentSlot.OFF_HAND)
                return;

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName()))
                if (entity instanceof Player)
                    if (((Player) entity).getDisplayName() != null)
                        if (cPlayer.getStacker()) {
                            CustomPlayer ePlayer = PlayerUtils.getOrCreateCustomPlayer((Player) entity);

                            if (ePlayer.getStacker()) {
                                player.setPassenger(entity);

                                if (PlayerSettings.getInstance().getServerVersion().contains("1_9"))
                                    ReflectionUtils.sendMountPacket(player);
                            } else if (!ePlayer.getStacker())
                                if (ConfigurationManager.getMessages().getBoolean("Send.Target-Stacker-Disabled"))
                                    if (!isNPC)
                                        player.sendMessage(MessageConfiguration.get("Target-Stacker-Disabled"));

                        } else if (!cPlayer.getStacker())
                            if (ConfigurationManager.getMessages().getBoolean("Send.Player-Stacker-Disabled"))
                                if (!isNPC)
                                    player.sendMessage(MessageConfiguration.get("Player-Stacker-Disabled"));
    }

    @EventHandler
    public void onPlayerLaunch(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        Entity entity = player.getPassenger();

        if (Utilities.hasAuthMePlugin() && entity instanceof Player && !Utilities.isAuthenticated((Player) entity))
            return;

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    if (player.getPassenger() != null) {
                        entity.getVehicle().eject();

                        if (PlayerSettings.getInstance().getServerVersion().contains("1_9"))
                            ReflectionUtils.sendMountPacket(player);

                        Vector direction = player.getLocation().getDirection();
                        entity.setVelocity(direction.multiply(ConfigurationManager.getDefault().getInt("Stacker.launch-force")));
                        entity.setFallDistance(-10000.0F);
                    }
                }
            }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(event.getDamager().getWorld().getName())) {
                if (event.getDamager().getType() == EntityType.PLAYER) {
                    Player player = (Player) event.getDamager();

                    if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
                        return;

                    CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

                    if (cPlayer.getStacker()) {
                        event.setCancelled(true);
                    }
                }
            }
    }
}
