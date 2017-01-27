package me.limbo56.settings.listeners;

import com.statiocraft.jukebox.Shuffle;
import com.statiocraft.jukebox.SingleSong;
import com.statiocraft.jukebox.scJukeBox;
import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.config.MessageConfiguration;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.Updater;
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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Created by lim_bo56
 * On 8/27/2016
 * At 12:00 AM
 */

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (!cPlayer.containsPlayer()) {
            cPlayer.addPlayer();
        } else {
        	cPlayer.loadSettings();
        }

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                CustomPlayer oPlayer = Utilities.getOrCreateCustomPlayer(online);

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

            if (cPlayer.hasRadio() && player.hasPermission(Cache.RADIO_PERMISSION)) {
                int type = ConfigurationManager.getDefault().getInt("Radio.type");
                switch (type) {
                    case 1:
                        new Shuffle().addPlayer(player);
                        break;
                    case 2:
                        new SingleSong(scJukeBox.listSongs().get(new Random().nextInt(scJukeBox.listSongs().size()))).addPlayer(player);
                        break;
                    case 3:
                        scJukeBox.getRadio().addPlayer(player);
                        break;
                    default:
                        PlayerSettings.getInstance().log("Invalid Radio type. Please put a value between 1 and 3");
                        break;
                }
            }

            if (PlayerSettings.getInstance().getConfig().getBoolean("Update-Message"))
                if (player.isOp())
                    player.sendMessage(Updater.playerUpdater());
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        cPlayer.saveSettings();

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            if (Utilities.hasRadioPlugin()) {
                if (scJukeBox.getCurrentJukebox(player) != null)
                    scJukeBox.getCurrentJukebox(player).removePlayer(player);
            }
        }

        Cache.PLAYER_LIST.remove(player);
    }

    @EventHandler
    public void onGamemodeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

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

        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

            if (!cPlayer.hasChat()) {
                event.getRecipients().remove(player);
                event.setCancelled(true);
                player.sendMessage(MessageConfiguration.get("Chat-Disabled"));
            } else if (cPlayer.hasChat()) {
                event.getRecipients().add(player);
                event.setCancelled(false);

                for (Player online : Bukkit.getOnlinePlayers()) {
                    CustomPlayer customPlayer = Utilities.getOrCreateCustomPlayer(online);

                    if (!customPlayer.hasChat())
                        event.getRecipients().remove(online);
                }

            }


        }


    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        boolean hasCitizens = Utilities.hasCitizens();

        boolean isNPC = event.getRightClicked().hasMetadata("NPC");

        Entity entity = event.getRightClicked();

        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        if (Utilities.isVersion("v1_9_R1") || Utilities.isVersion("v1_9_R2") || Utilities.isVersion("v1_10_R1") || Utilities.isVersion("v1_11_R1"))
            if (event.getHand() == EquipmentSlot.OFF_HAND)
                return;

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName()))
            if (entity != null)
                if (entity instanceof Player)
                    if (((Player) entity).getDisplayName() != null)
                        if (cPlayer.hasStacker()) {

                            CustomPlayer ePlayer = Utilities.getOrCreateCustomPlayer((Player) entity);

                            if (ePlayer.hasStacker()) {

                                player.setPassenger(entity);

                                PlayerSettings.getInstance().getMount().sendMountPacket(player);

                            } else if (!ePlayer.hasStacker())
                                if (ConfigurationManager.getMessages().getBoolean("Send.Target-Stacker-Disabled"))
                                    if (hasCitizens) {
                                        if (!isNPC)
                                            player.sendMessage(MessageConfiguration.get("Target-Stacker-Disabled"));
                                    } else {
                                        player.sendMessage(MessageConfiguration.get("Target-Stacker-Disabled"));
                                    }
                        } else if (!cPlayer.hasStacker())
                            if (ConfigurationManager.getMessages().getBoolean("Send.Player-Stacker-Disabled"))
                                if (hasCitizens) {
                                    if (!isNPC)
                                        player.sendMessage(MessageConfiguration.get("Player-Stacker-Disabled"));
                                } else {
                                    player.sendMessage(MessageConfiguration.get("Player-Stacker-Disabled"));
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

                    PlayerSettings.getInstance().getMount().sendMountPacket(player);

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
                CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

                if (cPlayer.hasStacker()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
