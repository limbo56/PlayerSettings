package me.limbo56.playersettings.database.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class DatabaseConfiguration {
  protected final ConfigurationSection section;

  public DatabaseConfiguration(ConfigurationSection section) {
    this.section = section;
  }

  public String getDatabaseName() {
    return section.getString("database");
  }
}
