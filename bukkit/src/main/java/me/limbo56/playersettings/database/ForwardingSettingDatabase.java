package me.limbo56.playersettings.database;

import java.util.Collection;
import java.util.UUID;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.util.PluginLogger;

public class ForwardingSettingDatabase implements SettingsDatabase<DatabaseConfiguration> {
  protected SettingsDatabase<?> database;

  protected ForwardingSettingDatabase() {}

  @Override
  public void connect() {
    PluginLogger.info("Connecting data manager...");
    database.connect();
  }

  @Override
  public void disconnect() {
    PluginLogger.info("Disconnecting data manager...");
    database.disconnect();
  }

  @Override
  public Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> uuids) {
    return database.loadSettingWatchers(uuids);
  }

  @Override
  public void saveSettingWatchers(Collection<SettingWatcher> settingWatchers) {
    database.saveSettingWatchers(settingWatchers);
  }

  @Override
  public void putExtra(UUID uuid, Setting setting, String key, String value) {
    database.putExtra(uuid, setting, key, value);
  }

  @Override
  public String getExtra(UUID uuid, Setting setting, String key) {
    return database.getExtra(uuid, setting, key);
  }

  @Override
  public DatabaseConfiguration getConfiguration() {
    return database.getConfiguration();
  }

  @Override
  public boolean isConnected() {
    return database != null && database.isConnected();
  }
}
