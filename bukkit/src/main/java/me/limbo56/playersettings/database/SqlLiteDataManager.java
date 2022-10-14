package me.limbo56.playersettings.database;

import static me.limbo56.playersettings.PlayerSettingsProvider.getPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.database.tasks.CreateTableTask;
import me.limbo56.playersettings.database.tasks.LoadUsersTask;
import me.limbo56.playersettings.database.tasks.SaveUsersTask;
import me.limbo56.playersettings.user.SettingUser;

public class SqlLiteDataManager implements DataManager {

  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private final String databaseName;
  private File databaseFile;

  public SqlLiteDataManager() {
    this.databaseName = getPlugin().getPluginConfiguration().getString("storage.database");
  }

  @Override
  public void connect() {
    File databaseFile = new File(plugin.getDataFolder(), this.databaseName + ".db");
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (IOException e) {
        plugin.getLogger().severe("Error while creating file: " + databaseName + ".db");
      }
    }
    this.databaseFile = databaseFile;
  }

  @Override
  public void disconnect() {
    this.databaseFile = null;
  }

  @Override
  public synchronized void loadUsers(Collection<SettingUser> users) {
    new LoadUsersTask(plugin, this.getConnection(), users).run();
  }

  @Override
  public synchronized void saveUsers(Collection<SettingUser> users) {
    new SaveUsersTask(plugin, this.getConnection(), users, "sqlite").run();
  }

  public synchronized void createDefaultTable() {
    new CreateTableTask(plugin, this.getConnection()).run();
  }

  private Connection getConnection() {
    try {
      Class.forName("org.sqlite.JDBC");
      return DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
    } catch (ClassNotFoundException ex) {
      plugin.getLogger().severe("Couldn't load SQLite JBDC library at runtime.");
    }
    return null;
  }
}
