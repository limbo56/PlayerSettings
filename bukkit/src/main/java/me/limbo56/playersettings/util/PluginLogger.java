package me.limbo56.playersettings.util;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class PluginLogger {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public static void log(String... messages) {
    Bukkit.getConsoleSender()
        .sendMessage("[" + PLUGIN.getName() + "] " + String.join("", messages));
  }

  public static void warning(String message) {
    log("[WARNING] ", message);
  }

  public static void severe(String message) {
    log("[SEVERE] ", message);
  }

  public static void debug(String message) {
    if (PLUGIN.getPluginConfiguration().hasDebugEnabled()) {
      log(ChatColor.WHITE + "[DEBUG] ", ChatColor.RESET + message);
    }
  }
}
