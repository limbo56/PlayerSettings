package me.limbo56.playersettings.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.database.configuration.DatabaseConfiguration;
import me.limbo56.playersettings.database.sql.*;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;

public class SQLiteSettingsDatabase implements SettingsDatabase<DatabaseConfiguration> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final DatabaseConfiguration databaseConfiguration;
  private File databaseFile;

  public SQLiteSettingsDatabase(ConfigurationSection section) {
    this.databaseConfiguration = new DatabaseConfiguration(section);
  }

  @Override
  public void connect() {
    String databaseName = getDatabaseConfiguration().getDatabaseName();
    File databaseFile = new File(PLUGIN.getDataFolder(), databaseName + ".db");
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (IOException exception) {
        PluginLogger.severe("Error while creating file: " + databaseName + ".db");
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
  public Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> users) {
    try (Connection connection = this.getConnection()) {
      return new LoadUsersQuery(connection, users).query();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while loading user settings");
      exception.printStackTrace();
      return Collections.emptyList();
    }
  }

  @Override
  public void saveSettingWatchers(Collection<SettingWatcher> settingWatchers) {
    try (Connection connection = this.getConnection()) {
      new SaveUsersTask(connection, settingWatchers, "sqlite").execute();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while saving user settings");
      exception.printStackTrace();
    }
  }

  @Override
  public void putExtra(UUID uuid, Setting setting, String key, String value) {
    try (Connection connection = this.getConnection()) {
      new SaveExtraTask(connection, uuid, setting, key, value, "sqlite").execute();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while saving extra data");
      exception.printStackTrace();
    }
  }

  @Override
  public String getExtra(UUID uuid, Setting setting, String key) {
    try (Connection connection = this.getConnection()) {
      return new GetExtraQuery(connection, uuid, setting, key).query();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while loading user settings");
      exception.printStackTrace();
      return null;
    }
  }

  @Override
  public DatabaseConfiguration getDatabaseConfiguration() {
    return databaseConfiguration;
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
    } catch (SQLException exception) {
      PLUGIN
          .getLogger()
          .log(Level.SEVERE, "An exception occurred while pooling an SQLite connection", exception);
    } catch (ClassNotFoundException ex) {
      PluginLogger.severe("Could not load SQLite JBDC library at runtime!");
    }
    return null;
  }
}
