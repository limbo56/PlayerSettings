package me.limbo56.playersettings.database.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class MongoDatabaseConfiguration extends DatabaseConfiguration {
  public MongoDatabaseConfiguration(ConfigurationSection section) {
    super(section);
  }

  public String getConnectionURI() {
    return section.getString("host");
  }
}
