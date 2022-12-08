package me.limbo56.playersettings.database;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.database.configuration.BaseDatabaseConfiguration;
import me.limbo56.playersettings.database.configuration.DatabaseConfiguration;
import me.limbo56.playersettings.database.process.CreateTableTask;
import me.limbo56.playersettings.database.process.LoadUsersQuery;
import me.limbo56.playersettings.database.process.SaveUsersTask;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;

public class SQLiteDatabase extends SettingsDatabase<DatabaseConfiguration> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private File databaseFile;

  public SQLiteDatabase(ConfigurationSection section) {
    super(new BaseDatabaseConfiguration(section));
  }

  @Override
  public void connect() {
    String databaseName = databaseConfiguration.getDatabaseName();
    File databaseFile = new File(PLUGIN.getDataFolder(), databaseName + ".db");
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (IOException e) {
        PLUGIN.getLogger().severe("Error while creating file: " + databaseName + ".db");
      }
    }
    this.databaseFile = databaseFile;
    this.createDefaultTable();
  }

  @Override
  public void disconnect() {
    this.databaseFile = null;
  }

  @Override
  public Collection<SettingWatcher> loadUserSettings(Collection<UUID> users) {
    try (Connection connection = this.getConnection()) {
      return new LoadUsersQuery(connection, users).query();
    } catch (SQLException e) {
      PLUGIN.getLogger().severe("An exception occurred while loading user settings");
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  @Override
  public void saveUserSettings(Collection<SettingWatcher> settings) {
    try (Connection connection = this.getConnection()) {
      new SaveUsersTask(connection, settings, "sqlite").execute();
    } catch (SQLException e) {
      PLUGIN.getLogger().severe("An exception occurred while saving user settings");
      e.printStackTrace();
    }
  }

  public void createDefaultTable() {
    try (Connection connection = this.getConnection()) {
      new CreateTableTask(connection).execute();
    } catch (SQLException exception) {
      PLUGIN
          .getLogger()
          .severe("An exception occurred while creating table 'playersettings_settings'");
      exception.printStackTrace();
    }
  }

  private Connection getConnection() {
    try {
      Class.forName("org.sqlite.JDBC");
      return DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
    } catch (SQLException ex) {
      PLUGIN
          .getLogger()
          .log(Level.SEVERE, "An exception occurred while pooling an SQLite connection", ex);
    } catch (ClassNotFoundException ex) {
      PLUGIN.getLogger().severe("Could not load SQLite JBDC library at runtime!");
    }
    return null;
  }
}
