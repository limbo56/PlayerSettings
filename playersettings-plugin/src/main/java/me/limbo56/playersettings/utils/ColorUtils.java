package me.limbo56.playersettings.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class ColorUtils {
    public static String translateString(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translateStringList(List<String> input) {
        return input.stream().map(ColorUtils::translateString).collect(Collectors.toList());
    }
}
