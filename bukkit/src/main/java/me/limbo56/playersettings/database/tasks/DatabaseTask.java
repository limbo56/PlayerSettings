package me.limbo56.playersettings.database.tasks;

import java.sql.Connection;
import me.limbo56.playersettings.PlayerSettings;

public abstract class DatabaseTask implements Runnable {
  protected final PlayerSettings plugin;
  protected final Connection connection;

  protected DatabaseTask(PlayerSettings plugin, Connection connection) {
    this.plugin = plugin;
    this.connection = connection;
  }
}
