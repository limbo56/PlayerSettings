package me.limbo56.playersettings.database;

import org.bukkit.configuration.ConfigurationSection;

public class SettingsDatabaseProvider {
  public static SettingsDatabase<?> getSettingsDatabase(ConfigurationSection configuration) {
    String type = configuration.getString("type", "sqlite");
    if (type.equalsIgnoreCase("sql")) {
      return new SQLSettingsDatabase(configuration);
    } else if (type.equalsIgnoreCase("sqlite")) {
      return new SQLiteSettingsDatabase(configuration);
    } else if (type.equalsIgnoreCase("mongodb")) {
      return new MongoSettingsDatabase(configuration);
    } else {
      throw new IllegalArgumentException("Not a valid database type: " + type);
    }
  }
}
