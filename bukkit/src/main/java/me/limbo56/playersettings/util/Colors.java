package me.limbo56.playersettings.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class Colors {
  private static final Pattern HEX_COLOR = Pattern.compile("&x#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");

  public static String translateColorCodes(String input) {
    String textToTranslate =
        Version.getCurrentVersion().isOlderThan("1.16") ? input : translateHexColors(input);
    return ChatColor.translateAlternateColorCodes('&', textToTranslate);
  }

  private static String translateHexColors(String input) {
    // Match for hex colors
    Matcher matcher = HEX_COLOR.matcher(input);
    StringBuffer translatedInputBuffer = new StringBuffer();
    while (matcher.find()) {
      // Append replacement
      String replacement = matcher.group();
      int hexPrefixAfter = replacement.indexOf("#") + 1;
      String hexChar = replacement.substring(0, hexPrefixAfter).replace("#", "&");
      String hexColor = String.join("&", replacement.substring(hexPrefixAfter).split(""));
      matcher.appendReplacement(translatedInputBuffer, hexChar + hexColor);
    }

    // Append all hex color replacements to the translated buffer
    matcher.appendTail(translatedInputBuffer);
    return translatedInputBuffer.toString();
  }
}
