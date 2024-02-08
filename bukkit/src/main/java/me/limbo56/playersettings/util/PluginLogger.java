package me.limbo56.playersettings.util;

import java.util.logging.Level;
import me.limbo56.playersettings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class PluginLogger {
  private static final PlayerSettings PLUGIN = PlayerSettings.getInstance();
  private static final ChatColor DEBUG_COLOR = ChatColor.WHITE;
  private static final ChatColor RESET_COLOR = ChatColor.RESET;

  private PluginLogger() {}

  public static void info(String message) {
    log(Level.INFO, message);
  }

  public static void warning(String message) {
    log(Level.WARNING, message);
  }

  public static void severe(String message) {
    log(Level.SEVERE, message);
  }

  public static void severe(String message, Throwable throwable) {
    log(Level.SEVERE, message, throwable);
  }

  public static void debug(String message) {
    if (PLUGIN.getConfiguration().hasDebugEnabled()) {
      logColored(DEBUG_COLOR + "[DEBUG] " + RESET_COLOR + message);
    }
  }

  public static void log(Level level, String message) {
    PLUGIN.getLogger().log(level, message);
  }

  public static void log(Level level, String message, Throwable throwable) {
    PLUGIN.getLogger().log(level, message, throwable);
  }

  public static void logColored(String... messages) {
    Bukkit.getConsoleSender()
        .sendMessage("[" + PLUGIN.getName() + "] " + String.join("", messages));
  }

  public static void logElapsedMillis(String message, Timer timer) {
    long millisDuration = timer.getElapsedDuration().toMillis();
    info(message + " (took " + millisDuration + "ms)");
  }

  public static void logVersionMessage() {
    Messages.getUpdateMessage()
        .thenAcceptAsync(
            message -> message.forEach(text -> logColored(Colors.translateColorCodes(text))))
        .exceptionally(
            exception -> {
              PluginLogger.severe(
                  "An exception occurred while logging the version message", exception);
              return null;
            });
  }
}
