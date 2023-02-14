package me.limbo56.playersettings.database;

import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.database.configuration.DatabaseConfiguration;

import java.util.Collection;
import java.util.UUID;

public interface SettingsDatabase<T extends DatabaseConfiguration> {
  void connect();

  void disconnect();

  Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> uuids);

  void saveSettingWatchers(Collection<SettingWatcher> settingWatchers);

  void putExtra(UUID uuid, Setting setting, String key, String value);

  String getExtra(UUID uuid, Setting setting, String key);

  T getDatabaseConfiguration();
}
