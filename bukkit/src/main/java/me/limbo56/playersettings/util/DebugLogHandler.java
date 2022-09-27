package me.limbo56.playersettings.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DebugLogHandler extends Handler {
  @Override
  public void publish(LogRecord record) {
    Level loggerLevel = PlayerSettingsProvider.getPlugin().getLogger().getLevel();
    if (loggerLevel == Level.ALL) {
      record.setLevel(Level.OFF);
      Bukkit.getServer().getConsoleSender().sendMessage(record.getMessage());
      return;
    }

    Level recordLevel = record.getLevel();
    if (recordLevel.intValue() >= Level.INFO.intValue()) {
      record.setLevel(Level.OFF);
      Bukkit.getServer().getConsoleSender().sendMessage(record.getMessage());
      return;
    }

    String levelName = ChatColor.WHITE + "[" + recordLevel.getLocalizedName() + "]";
    Bukkit.getServer()
        .getConsoleSender()
        .sendMessage(levelName + ChatColor.RESET + record.getMessage());
    record.setLevel(Level.OFF);
  }

  @Override
  public void flush() {}

  @Override
  public void close() throws SecurityException {}
}
