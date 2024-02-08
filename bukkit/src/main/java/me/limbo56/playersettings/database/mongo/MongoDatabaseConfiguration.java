package me.limbo56.playersettings.database.mongo;

import me.limbo56.playersettings.database.DatabaseConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public class MongoDatabaseConfiguration extends DatabaseConfiguration {
  public MongoDatabaseConfiguration(ConfigurationSection section) {
    super(section);
  }

  public String getConnectionURI() {
    return configuration.getString("host");
  }
}
