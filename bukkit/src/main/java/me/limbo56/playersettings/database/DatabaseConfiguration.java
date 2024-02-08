package me.limbo56.playersettings.database;

import org.bukkit.configuration.ConfigurationSection;

public class DatabaseConfiguration {
  protected final ConfigurationSection configuration;

  public DatabaseConfiguration(ConfigurationSection configuration) {
    this.configuration = configuration;
  }

  public String getDatabaseName() {
    return configuration.getString("database");
  }
}
