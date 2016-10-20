package me.lim_bo56.settings.utils;

import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map.Entry;
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

        if (serverVersion.equals(version))
            return true;
        else
            return false;

    }

    public static void addToDefault(Player player) {
        if (Variables.defaultVisibility) Variables.VISIBILITY_LIST.add(player.getUniqueId());
        if (Variables.defaultStacker) Variables.STACKER_LIST.add(player.getUniqueId());
        if (Variables.defaultChat) Variables.CHAT_LIST.add(player.getUniqueId());
        if (Variables.defaultVanish) Variables.VANISH_LIST.add(player.getUniqueId());
        if (Variables.defaultFly) Variables.FLY_LIST.add(player.getUniqueId());
        if (Variables.defaultSpeed) Variables.SPEED_LIST.add(player.getUniqueId());
        if (Variables.defaultJump) Variables.JUMP_LIST.add(player.getUniqueId());
    }

    public static void loadOnlinePlayers() {

        ConfigurationManager menu = ConfigurationManager.getMenu();

        if (Bukkit.getOnlinePlayers() != null)

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {

                    CustomPlayer cPlayer = new CustomPlayer(player);

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

                } else if (!menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        CustomPlayer oPlayer = new CustomPlayer(online);

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