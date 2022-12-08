package me.limbo56.playersettings.database.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class BaseDatabaseConfiguration implements DatabaseConfiguration {
  protected final ConfigurationSection section;

  public BaseDatabaseConfiguration(ConfigurationSection section) {
    this.section = section;
  }

  @Override
  public String getDatabaseName() {
    return section.getString("database");
  }
}
