package me.limbo56.playersettings.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.database.SettingsDatabase;
import me.limbo56.playersettings.database.sql.query.GetExtraQuery;
import me.limbo56.playersettings.database.sql.query.LoadUsersQuery;
import me.limbo56.playersettings.database.sql.task.CreateTableTask;
import me.limbo56.playersettings.database.sql.task.SaveExtraTask;
import me.limbo56.playersettings.database.sql.task.SaveUsersTask;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;

public class SQLSettingsDatabase implements SettingsDatabase<SQLDatabaseConfiguration> {
  private final SQLDatabaseConfiguration databaseConfiguration;
  private HikariDataSource hikariDataSource;

  public SQLSettingsDatabase(ConfigurationSection section) {
    this.databaseConfiguration = new SQLDatabaseConfiguration(section);
  }

  @Override
  public void connect() {
    if (isConnected()) {
      disconnect();
    }

    hikariDataSource = new HikariDataSource(getConfiguration().getPoolConfiguration());
    this.createDefaultTable();
  }

  @Override
  public void disconnect() {
    if (isConnected()) {
      hikariDataSource.close();
      hikariDataSource = null;
    }
  }

  @Override
  public Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> users) {
    try (Connection connection = this.getConnection()) {
      return new LoadUsersQuery(connection, users).query();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while loading user settings", exception);
      return Collections.emptyList();
    }
  }

  @Override
  public void saveSettingWatchers(Collection<SettingWatcher> settingWatchers) {
    try (Connection connection = this.getConnection()) {
      new SaveUsersTask(connection, settingWatchers, "sql").execute();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while saving user settings", exception);
    }
  }

  @Override
  public void putExtra(UUID uuid, Setting setting, String key, String value) {
    try (Connection connection = this.getConnection()) {
      new SaveExtraTask(connection, uuid, setting, key, value, "sql").execute();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while saving user settings", exception);
    }
  }

  @Override
  public String getExtra(UUID uuid, Setting setting, String key) {
    try (Connection connection = this.getConnection()) {
      return new GetExtraQuery(connection, uuid, setting, key).query();
    } catch (SQLException exception) {
      PluginLogger.severe("An exception occurred while loading user settings", exception);
      return null;
    }
  }

  @Override
  public SQLDatabaseConfiguration getConfiguration() {
    return databaseConfiguration;
  }

  @Override
  public boolean isConnected() {
    return hikariDataSource != null && hikariDataSource.isRunning();
  }

  protected void createDefaultTable() {
    try (Connection connection = this.getConnection()) {
      new CreateTableTask(connection).execute();
    } catch (SQLException exception) {
      PluginLogger.severe(
          "An exception occurred while creating table 'playersettings_settings'", exception);
    }
  }

  protected Connection getConnection() {
    try {
      return hikariDataSource.getConnection();
    } catch (SQLException exception) {
      PluginLogger.severe(
          "An exception occurred while pooling an SQL database connection", exception);
    }
    return null;
  }
}
