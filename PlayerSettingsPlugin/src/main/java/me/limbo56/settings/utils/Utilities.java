package me.limbo56.settings.utils;

import com.statiocraft.jukebox.Shuffle;
import com.statiocraft.jukebox.SingleSong;
import com.statiocraft.jukebox.scJukeBox;
import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:27:57 AM
 */
public class Utilities {

    /**
     * @param sender     Command sender.
     * @param map        The StoredMap.
     * @param page       Page number.
     * @param pageLength Page length.
     */
    public static void paginate(CommandSender sender, SortedMap<Integer, String> map, int page, int pageLength) { //Credits to gomeow
        sender.sendMessage(ChatColor.YELLOW + "List: Page (" + String.valueOf(page) + " of " + (((map.size() % pageLength) == 0) ? map.size() / pageLength : (map.size() / pageLength) + 1) + ")");
        int i = 0;
        int k = 0;
        page--;

        for (final Entry<Integer, String> e : map.entrySet()) {
            k += 1;
            if ((((page * pageLength) + i + 1) == k) && (k != ((page * pageLength) + pageLength + 1))) {
                i += 1;
                sender.sendMessage(ChatColor.YELLOW + " - " + e.getValue());
            }
        }
    }

    /**
     * Check if server is using the specified version.
     *
     * @param version Server version.
     * @return boolean
     */
    public static boolean isVersion(String version) {
        String serverVersion;

        try {

            serverVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }

        return serverVersion.equals(version);

    }

    public static boolean hasRadioPlugin() {
        return PlayerSettings.getInstance().getServer().getPluginManager().getPlugin("icJukeBox") != null;
    }

    public static boolean hasCitizens() {
        return PlayerSettings.getInstance().getServer().getPluginManager().getPlugin("Citizens") != null;
    }
    
    public static CustomPlayer getOrCreateCustomPlayer(Player player) {
    	if (!Cache.PLAYER_LIST.containsKey(player))
    		Cache.PLAYER_LIST.put(player, new CustomPlayer(player));

    	return Cache.PLAYER_LIST.get(player);
    }

    public static void loadOnlinePlayers() {

        if (Bukkit.getOnlinePlayers() != null)

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

                    CustomPlayer cPlayer = getOrCreateCustomPlayer(player);

                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.removePotionEffect(PotionEffectType.JUMP);
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);

                    cPlayer.loadSettings();

                    if (cPlayer.hasVisibility()) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(online);
                        }
                    }

                    if (cPlayer.hasVanish()) {

                        player.addPotionEffect(Cache.INVISIBILITY);

                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.hidePlayer(player);
                        }

                    }

                    if (cPlayer.hasFly()) {
                        player.setAllowFlight(true);
                    }

                    if (cPlayer.hasSpeed()) {
                        player.addPotionEffect(Cache.SPEED);
                    }

                    if (cPlayer.hasJump()) {
                        player.addPotionEffect(Cache.JUMP);
                    }

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

                    if (player.isOp()) {
                        player.sendMessage(Updater.playerUpdater());
                    }

                } else if (!Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        CustomPlayer oPlayer = getOrCreateCustomPlayer(online);

                        if (oPlayer.hasVanish()) {
                            online.hidePlayer(player);
                        } else {
                            player.showPlayer(online);
                        }

                    }
                }

            }

    }

}