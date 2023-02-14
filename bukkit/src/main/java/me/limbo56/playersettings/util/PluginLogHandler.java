package me.limbo56.playersettings.util;

import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PluginLogHandler extends Handler {
  public static void log(String message) {
    log("[" + PlayerSettingsProvider.getPlugin().getName() + "] ", message);
  }

  private static void log(String prefix, String message) {
    Bukkit.getServer().getConsoleSender().sendMessage(prefix + message);
  }

  @Override
  public void publish(LogRecord record) {
    Level loggerLevel = PlayerSettingsProvider.getPlugin().getLogger().getLevel();
    Level recordLevel = record.getLevel();
    if (loggerLevel == Level.ALL || recordLevel.intValue() >= Level.INFO.intValue()) {
      return;
    }

    record.setLevel(Level.OFF);
    log(ChatColor.WHITE + "[DEBUG]", ChatColor.RESET + record.getMessage());
  }

  @Override
  public void flush() {}

  @Override
  public void close() throws SecurityException {}
}
