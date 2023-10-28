package me.limbo56.playersettings.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.database.sql.*;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;

public class SQLiteSettingsDatabase extends SQLSettingsDatabase {

  public SQLiteSettingsDatabase(ConfigurationSection section) {
    super(section);
  }

  @Override
  public void connect() {
    String databaseName = getDatabaseConfiguration().getDatabaseName();
    File databaseFile = new File(PLUGIN.getDataFolder(), databaseName + ".db");
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (IOException exception) {
        PluginLogger.severe("Error while creating database: " + databaseName + ".db");
      }
    }

    super.connect();
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
}
