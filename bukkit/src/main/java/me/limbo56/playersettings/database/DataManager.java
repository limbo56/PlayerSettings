package me.limbo56.playersettings.database;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.SettingWatcher;

public class DataManager extends ForwardingSettingDatabase {
  private final PlayerSettings plugin;

  public DataManager(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  @Override
  public void connect() {
    if (!isConnected()) {
      database = plugin.getConfiguration().getSettingsDatabase();
    }
    super.connect();
  }

  public SettingWatcher loadSettingWatcher(UUID uuid) {
    List<UUID> uuids = Collections.singletonList(uuid);
    for (SettingWatcher settingWatcher : loadSettingWatchers(uuids)) {
      return settingWatcher;
    }
    return null;
  }
}
