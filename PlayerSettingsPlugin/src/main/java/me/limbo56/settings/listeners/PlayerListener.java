package me.limbo56.settings.listeners;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.config.MessageConfiguration;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
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

        Utilities.loadSettings(player);
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        Utilities.saveSettings(player);
    }

    @EventHandler
    public void onGamemodeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName()))
                if (event.getNewGameMode() == GameMode.ADVENTURE || event.getNewGameMode() == GameMode.SURVIVAL) {
                    cPlayer.setFly(ConfigurationManager.getDefault().getBoolean("Default.Fly"));
                    if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled"))
                        cPlayer.setDoubleJump(ConfigurationManager.getDefault().getBoolean("Default.DoubleJump"));
                } else if (event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR) {
                    cPlayer.setFly(true);
                    if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled"))
                        cPlayer.setDoubleJump(false);
                }

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayersChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
            return;

        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Chat.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (!cPlayer.hasChat()) {
                    event.getRecipients().remove(player);
                    event.setCancelled(true);
                    player.sendMessage(MessageConfiguration.get("Chat-Disabled"));
                } else if (cPlayer.hasChat()) {
                    event.getRecipients().add(player);

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        CustomPlayer customPlayer = Utilities.getOrCreateCustomPlayer(online);

                        if (!customPlayer.hasChat())
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

        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled")) {
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (cPlayer.hasFly())
                    return;
                if (cPlayer.hasDoubleJump()) {
                    if (player.getGameMode() != GameMode.CREATIVE && !player.getLocation().subtract(0D, 1D, 0D).getBlock().isEmpty()) {
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

        boolean isNPC = event.getRightClicked().hasMetadata("NPC");

        Entity entity = event.getRightClicked();
        if (Utilities.hasAuthMePlugin() && entity instanceof Player && !Utilities.isAuthenticated((Player)entity))
            return;

        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (Utilities.isVersion("v1_9_R1") || Utilities.isVersion("v1_9_R2") || Utilities.isVersion("v1_10_R1") || Utilities.isVersion("v1_11_R1"))
            if (event.getHand() == EquipmentSlot.OFF_HAND)
                return;

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName()))
                if (entity != null)
                    if (entity instanceof Player)
                        if (((Player) entity).getDisplayName() != null)
                            if (cPlayer.hasStacker()) {

                                CustomPlayer ePlayer = Utilities.getOrCreateCustomPlayer((Player) entity);

                                if (ePlayer.hasStacker()) {
                                    player.setPassenger(entity);
                                    PlayerSettings.getMount().sendMountPacket(player);
                                } else if (!ePlayer.hasStacker())
                                    if (ConfigurationManager.getMessages().getBoolean("Send.Target-Stacker-Disabled"))
                                        if (!isNPC)
                                            player.sendMessage(MessageConfiguration.get("Target-Stacker-Disabled"));
                            } else if (!cPlayer.hasStacker())
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
        if (Utilities.hasAuthMePlugin() && entity instanceof Player && !Utilities.isAuthenticated((Player)entity))
            return;

        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled"))
            if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    if (player.getPassenger() != null) {
                        entity.getVehicle().eject();
                        PlayerSettings.getMount().sendMountPacket(player);
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

                    CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

                    if (cPlayer.hasStacker()) {
                        event.setCancelled(true);
                    }
                }
            }
    }

}
