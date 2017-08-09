package me.limbo56.settings.utils;

import org.bukkit.ChatColor;

/**
 * Created by lim_bo56
 * On 8/14/2016
 * At 11:08 AM
 */
public class ColorUtils {

    /**
     * Translate color codes
     *
     * @param input Text to be translated
     * @return String
     */
    public static String Color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
