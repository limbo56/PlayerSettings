package me.limbo56.settings.utils;

import me.limbo56.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.SortedMap;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:27:57 AM
 */
@SuppressWarnings("deprecation")
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
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        return serverVersion.equals(version);
    }

    public static boolean hasRadioPlugin() {
        return PlayerSettings.getInstance().getServer().getPluginManager().getPlugin("icJukeBox") != null;
    }

    public static boolean hasAuthMePlugin() {
        return PlayerSettings.getInstance().getServer().getPluginManager().getPlugin("AuthMe") != null;
    }

    public static boolean isAuthenticated(Player player) {
        boolean auth;

        try {
            auth = isAuthenticatedReflection("fr.xephi.authme.api.v3.AuthMeApi", player);
        } catch (ClassNotFoundException e) {
            try {
                auth = isAuthenticatedReflection("fr.xephi.authme.api.NewAPI", player);
            } catch (ClassNotFoundException ex) {
                try {
                    auth = isAuthenticatedReflection("fr.xephi.authme.api.API", player);
                } catch (ClassNotFoundException exc) {
                    return false;
                }
            }
        }

        return auth;
    }

    private static boolean isAuthenticatedReflection(String location, Player player) throws ClassNotFoundException {
        try {
            Class authMeApi = Class.forName(location);
            Object authMeApiInstance = authMeApi.newInstance();
            Method isAuthenticatedMethod = authMeApiInstance.getClass().getMethod("isAuthenticated");

            return (boolean) isAuthenticatedMethod.invoke(player);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            return false;
        }
    }
}