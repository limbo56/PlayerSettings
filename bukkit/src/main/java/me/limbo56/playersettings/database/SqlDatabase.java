package me.limbo56.playersettings.database;

import com.zaxxer.hikari.HikariDataSource;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.database.configuration.SqlDatabaseConfiguration;
import me.limbo56.playersettings.database.process.CreateTableTask;
import me.limbo56.playersettings.database.process.LoadUsersQuery;
import me.limbo56.playersettings.database.process.SaveUsersTask;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;

public class SqlDatabase extends SettingsDatabase<SqlDatabaseConfiguration> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private HikariDataSource hikariDataSource;

  public SqlDatabase(ConfigurationSection section) {
    super(new SqlDatabaseConfiguration(section));
  }

  @Override
  public void connect() {
    hikariDataSource = new HikariDataSource(databaseConfiguration.getPoolConfiguration());
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
  public Collection<SettingWatcher> loadUserSettings(Collection<UUID> users) {
    try (Connection connection = this.getConnection()) {
      return new LoadUsersQuery(connection, users).query();
    } catch (SQLException exception) {
      PLUGIN.getLogger().severe("An exception occurred while loading user settings");
      exception.printStackTrace();
      return Collections.emptyList();
    }
  }

  @Override
  public void saveUserSettings(Collection<SettingWatcher> settings) {
    try (Connection connection = this.getConnection()) {
      new SaveUsersTask(connection, settings, "sql").execute();
    } catch (SQLException exception) {
      PLUGIN.getLogger().severe("An exception occurred while saving user settings");
      exception.printStackTrace();
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
      return hikariDataSource.getConnection();
    } catch (SQLException ex) {
      PLUGIN
          .getLogger()
          .log(Level.SEVERE, "An exception occurred while pooling an SQL database connection", ex);
    }
    return null;
  }
}
