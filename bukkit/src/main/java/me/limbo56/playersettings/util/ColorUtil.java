package me.limbo56.playersettings.util;

import java.util.List;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
  public static String translateColorCodes(String input) {
    return ChatColor.translateAlternateColorCodes('&', input);
  }

  public static List<String> translateColorCodes(List<String> input) {
    return input.stream().map(ColorUtil::translateColorCodes).collect(Collectors.toList());
  }
}
