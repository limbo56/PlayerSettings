package me.limbo56.playersettings.database;

import static me.limbo56.playersettings.database.DatabaseParser.DATABASE_PARSER;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.database.tasks.CreateTableTask;
import me.limbo56.playersettings.database.tasks.LoadUsersTask;
import me.limbo56.playersettings.database.tasks.SaveUsersTask;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.configuration.ConfigurationSection;

public class SqlDataManager implements DataManager {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private final HikariConfig hikariConfig;
  private HikariDataSource hikariDataSource;

  public SqlDataManager(ConfigurationSection section) {
    this.hikariConfig = DATABASE_PARSER.parse(section);
  }

  @Override
  public void connect() {
    hikariDataSource = new HikariDataSource(hikariConfig);
  }

  @Override
  public void disconnect() {
    if (hikariDataSource == null || !hikariDataSource.isRunning()) {
      return;
    }
    hikariDataSource.close();
  }

  @Override
  public synchronized void loadUsers(Collection<SettingUser> users) {
    new LoadUsersTask(plugin, this.getConnection(), users).run();
  }

  @Override
  public synchronized void saveUsers(Collection<SettingUser> users) {
    new SaveUsersTask(plugin, this.getConnection(), users).run();
  }

  public synchronized void createDefaultTable() {
    new CreateTableTask(plugin, this.getConnection()).run();
  }

  private Connection getConnection() {
    try {
      return hikariDataSource.getConnection();
    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE, "SQL exception on initialize", ex);
    }
    return null;
  }
}
