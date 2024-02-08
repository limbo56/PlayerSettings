package me.limbo56.playersettings.database;

import java.util.Collection;
import java.util.UUID;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;

public interface SettingsDatabase<T extends DatabaseConfiguration> {
  void connect();

  void disconnect();

  Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> uuids);

  void saveSettingWatchers(Collection<SettingWatcher> settingWatchers);

  void putExtra(UUID uuid, Setting setting, String key, String value);

  String getExtra(UUID uuid, Setting setting, String key);

  T getConfiguration();

  boolean isConnected();
}
