package me.limbo56.playersettings.database;

import com.zaxxer.hikari.HikariDataSource;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.database.configuration.SQLDatabaseConfiguration;
import me.limbo56.playersettings.database.sql.*;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;

public class SQLSettingsDatabase implements SettingsDatabase<SQLDatabaseConfiguration> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final SQLDatabaseConfiguration databaseConfiguration;
  private HikariDataSource hikariDataSource;

  public SQLSettingsDatabase(ConfigurationSection section) {
    this.databaseConfiguration = new SQLDatabaseConfiguration(section);
  }

  @Override
  public void connect() {
    hikariDataSource = new HikariDataSource(getDatabaseConfiguration().getPoolConfiguration());
    this.createDefaultTable();
  }

  @Override
  public void disconnect() {
    if (hikariDataSource == null || !hikariDataSource.isRunning()) {
      return;
    }
    hikariDataSource.close();
  }

  @Override
  public Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> users) {
    try (Connection connection = this.getConnection()) {
      return new LoadUsersQuery(connection, users).query();
    } catch (SQLException exception) {
      PLUGIN.getLogger().severe("An exception occurred while loading user settings");
      exception.printStackTrace();
      return Collections.emptyList();
    }
  }

  @Override
  public void saveSettingWatchers(Collection<SettingWatcher> settingWatchers) {
    try (Connection connection = this.getConnection()) {
      new SaveUsersTask(connection, settingWatchers, "sql").execute();
    } catch (SQLException exception) {
      PLUGIN.getLogger().severe("An exception occurred while saving user settings");
      exception.printStackTrace();
    }
  }

  @Override
  public void putExtra(UUID uuid, Setting setting, String key, String value) {
    try (Connection connection = this.getConnection()) {
      new SaveExtraTask(connection, uuid, setting, key, value, "sql").execute();
    } catch (SQLException e) {
      PLUGIN.getLogger().severe("An exception occurred while saving user settings");
      e.printStackTrace();
    }
  }

  @Override
  public String getExtra(UUID uuid, Setting setting, String key) {
    try (Connection connection = this.getConnection()) {
      return new GetExtraQuery(connection, uuid, setting, key).query();
    } catch (SQLException e) {
      PLUGIN.getLogger().severe("An exception occurred while loading user settings");
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public SQLDatabaseConfiguration getDatabaseConfiguration() {
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
      return hikariDataSource.getConnection();
    } catch (SQLException ex) {
      PLUGIN
          .getLogger()
          .log(Level.SEVERE, "An exception occurred while pooling an SQL database connection", ex);
    }
    return null;
  }
}
