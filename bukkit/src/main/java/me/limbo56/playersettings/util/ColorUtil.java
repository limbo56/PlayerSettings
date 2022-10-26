package me.limbo56.playersettings.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
  private static final Pattern HEX_COLOR = Pattern.compile("&x#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");

  public static String translateColorCodes(String input) {
    String textToTranslate =
        Version.getCurrentVersion().isOlderThan("1.16") ? input : translateHexColors(input);
    return ChatColor.translateAlternateColorCodes('&', textToTranslate);
  }

  public static List<String> translateColorCodes(List<String> input) {
    return input.stream().map(ColorUtil::translateColorCodes).collect(Collectors.toList());
  }

  private static String translateHexColors(String input) {
    Matcher matcher = HEX_COLOR.matcher(input);
    StringBuffer translatedInputBuilder = new StringBuffer();

    while (matcher.find()) {
      String replacement = matcher.group();
      int hexPrefixAfter = replacement.indexOf("#") + 1;
      String hexChar = replacement.substring(0, hexPrefixAfter).replace("#", "&");
      String hexColor = String.join("&", replacement.substring(hexPrefixAfter).split(""));

      matcher.appendReplacement(translatedInputBuilder, hexChar + hexColor);
    }
    matcher.appendTail(translatedInputBuilder);

    return translatedInputBuilder.toString();
  }
}
