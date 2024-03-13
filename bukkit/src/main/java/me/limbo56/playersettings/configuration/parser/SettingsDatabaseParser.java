package me.limbo56.playersettings.configuration.parser;

import me.limbo56.playersettings.database.SettingsDatabase;
import me.limbo56.playersettings.database.mongo.MongoSettingsDatabase;
import me.limbo56.playersettings.database.sql.SQLSettingsDatabase;
import me.limbo56.playersettings.database.sql.SQLiteSettingsDatabase;
import org.bukkit.configuration.ConfigurationSection;

public class SettingsDatabaseParser implements ConfigurationSectionParser<SettingsDatabase<?>> {
  @Override
  public SettingsDatabase<?> parse(ConfigurationSection section) {
    String type = section.getString("type", "sqlite");
    if (type.equalsIgnoreCase("sql")) {
      return new SQLSettingsDatabase(section);
    } else if (type.equalsIgnoreCase("sqlite")) {
      return new SQLiteSettingsDatabase(section);
    } else if (type.equalsIgnoreCase("mongodb")) {
      return new MongoSettingsDatabase(section);
    } else {
      throw new IllegalArgumentException("Not a valid database type: " + type);
    }
  }
}
