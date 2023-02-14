package me.limbo56.playersettings.database.sql;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LoadUsersQuery implements SqlDatabaseQuery<Collection<SettingWatcher>> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private static final String LOAD_QUERY = "SELECT * FROM playersettings_settings WHERE owner=?";
  private final Connection connection;
  private final Collection<UUID> uuids;

  public LoadUsersQuery(Connection connection, Collection<UUID> uuids) {
    this.connection = connection;
    this.uuids = uuids;
  }

  @Override
  public Collection<SettingWatcher> query() throws SQLException {
    Collection<SettingWatcher> settingWatcherList = new ArrayList<>();
    for (UUID uuid : uuids) {
      SettingWatcher settingWatcher = PLUGIN.getUserManager().getSettingWatcher(uuid);
      PreparedStatement loadStatement = connection.prepareStatement(LOAD_QUERY);
      loadStatement.setString(1, uuid.toString());

      ResultSet resultSet = loadStatement.executeQuery();
      while (resultSet.next()) {
        String settingName = resultSet.getString("settingName");
        int value = resultSet.getInt("value");

        // Don't load settings that don't exist or aren't enabled
        Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
        if (setting == null || !setting.isEnabled()) {
          continue;
        }
        settingWatcher.setValue(settingName, value, true);
      }

      settingWatcherList.add(settingWatcher);
    }
    return settingWatcherList;
  }
}
