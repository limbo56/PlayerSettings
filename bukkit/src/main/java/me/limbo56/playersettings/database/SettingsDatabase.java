package me.limbo56.playersettings.database;

import me.limbo56.playersettings.database.configuration.DatabaseConfiguration;

public abstract class SettingsDatabase<T extends DatabaseConfiguration>
    implements DatabaseConnector, SettingsDataManager {
  protected final T databaseConfiguration;

  public SettingsDatabase(T databaseConfiguration) {
    this.databaseConfiguration = databaseConfiguration;
  }
}
