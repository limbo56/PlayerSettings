package me.limbo56.settings.utils;

import com.statiocraft.jukebox.Shuffle;
import com.statiocraft.jukebox.SingleSong;
import com.statiocraft.jukebox.scJukeBox;
import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.managers.PlayerManager;
import me.limbo56.settings.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:27:30 AM
 */
public class PlayerUtils {

    private static PlayerManager playerManager = PlayerSettings.getPlayerManager();

    /**
     * Method to send a message to a command sender.
     *
     * @param sender  Command sender
     * @param message The message
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.isOnline()) {
                player.sendMessage(message);
            }
        } else {
            sender.sendMessage(message);
        }
    }

    public static CustomPlayer getOrCreateCustomPlayer(Player player) {
        if (!playerManager.getPlayerList().containsKey(player.getUniqueId()))
            playerManager.getPlayerList().put(player.getUniqueId(), new CustomPlayer(player));

        return playerManager.getPlayerList().get(player.getUniqueId());
    }

    public static void loadSettings(Player player) {
        CustomPlayer cPlayer = getOrCreateCustomPlayer(player);

        if (!playerManager.containsPlayer(cPlayer)) {
            playerManager.addPlayer(cPlayer);
        } else {
            playerManager.loadSettings(cPlayer);
        }

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                CustomPlayer oPlayer = getOrCreateCustomPlayer(online);

                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Visibility.Enabled"))
                    if (!oPlayer.getVisibility())
                        online.hidePlayer(player);

                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled"))
                    if (oPlayer.getVanish())
                        online.hidePlayer(player);
            }

            for (Player online : Bukkit.getOnlinePlayers()) {
                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled"))
                    if (cPlayer.getVisibility()) {
                        player.showPlayer(online);
                    } else if (!cPlayer.getVisibility()) {
                        player.hidePlayer(online);
                    }
            }

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled"))
                if (cPlayer.getVanish()) {
                    player.addPotionEffect(Cache.INVISIBILITY);

                    for (Player online : Bukkit.getOnlinePlayers())
                        online.hidePlayer(player);
                } else {
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                }

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
                if (cPlayer.getFly())
                    player.setAllowFlight(true);

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Speed.Enabled"))
                if (cPlayer.getSpeed())
                    player.addPotionEffect(Cache.SPEED);
                else
                    player.removePotionEffect(PotionEffectType.SPEED);

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Jump.Enabled"))
                if (cPlayer.getJump())
                    player.addPotionEffect(Cache.JUMP);
                else
                    player.removePotionEffect(PotionEffectType.JUMP);

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Radio.Enabled"))
                if (cPlayer.getRadio()) {
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
                    Updater.sendUpdater(player);
        }
    }

    public static void saveSettings(Player player) {
        CustomPlayer cPlayer = getOrCreateCustomPlayer(player);

        if (!player.hasPermission(Cache.CHAT_PERMISSION))
            cPlayer.setChat(false);
        if (!player.hasPermission(Cache.DOUBLEJUMP_PERMISSION))
            cPlayer.setDoubleJump(false);
        if (!player.hasPermission(Cache.FLY_PERMISSION))
            cPlayer.setFly(false);
        if (!player.hasPermission(Cache.JUMP_PERMISSION))
            cPlayer.setJump(false);
        if (!player.hasPermission(Cache.RADIO_PERMISSION))
            cPlayer.setRadio(false);
        if (!player.hasPermission(Cache.SPEED_PERMISSION))
            cPlayer.setSpeed(false);
        if (!player.hasPermission(Cache.STACKER_PERMISSION))
            cPlayer.setStacker(false);
        if (!player.hasPermission(Cache.VANISH_PERMISSION))
            cPlayer.setVanish(false);
        if (!player.hasPermission(Cache.VISIBILITY_PERMISSION))
            cPlayer.setVisibility(false);

        playerManager.saveSettingsAsync(cPlayer);

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);

            if (Utilities.hasRadioPlugin()) {
                if (scJukeBox.getCurrentJukebox(player) != null)
                    scJukeBox.getCurrentJukebox(player).removePlayer(player);
            }

            if (cPlayer.getDoubleJump())
                player.setAllowFlight(false);
        }

        playerManager.getPlayerList().remove(player.getUniqueId());
    }

    public static void loadOnlinePlayers() {
        if (Bukkit.getOnlinePlayers() != null)
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (playerManager.getPlayerList().containsKey(player.getUniqueId()))
                    saveSettings(player);

                CustomPlayer cPlayer = getOrCreateCustomPlayer(player);

                if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                    if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
                        return;

                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.removePotionEffect(PotionEffectType.JUMP);
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);

                    if (!playerManager.containsPlayer(cPlayer))
                        playerManager.addPlayer(cPlayer);
                    else
                        playerManager.loadSettings(cPlayer);

                    if (cPlayer.getVisibility()) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(online);
                        }
                    }

                    if (cPlayer.getVanish()) {
                        player.addPotionEffect(Cache.INVISIBILITY);

                        for (Player online : Bukkit.getOnlinePlayers())
                            online.hidePlayer(player);
                    }

                    if (cPlayer.getFly())
                        player.setAllowFlight(true);

                    if (cPlayer.getSpeed())
                        player.addPotionEffect(Cache.SPEED);

                    if (cPlayer.getJump())
                        player.addPotionEffect(Cache.JUMP);

                    if (cPlayer.getRadio() && player.hasPermission(Cache.RADIO_PERMISSION)) {
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

                    if (player.isOp())
                        Updater.sendUpdater(player);

                } else if (!Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        CustomPlayer oPlayer = getOrCreateCustomPlayer(online);

                        if (oPlayer.getVanish()) {
                            online.hidePlayer(player);
                        } else {
                            player.showPlayer(online);
                        }

                        if (cPlayer.getDoubleJump())
                            player.setAllowFlight(false);
                    }
                }
            }
    }

}
