package me.limbo56.playersettings.database;

import java.util.Collection;
import java.util.UUID;

import me.limbo56.playersettings.api.setting.SettingWatcher;

public interface SettingsDataManager {
  Collection<SettingWatcher> loadUserSettings(Collection<UUID> uuids);

  void saveUserSettings(Collection<SettingWatcher> users);
}
