package me.limbo56.playersettings.database.sql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.UserManager;

public class LoadUsersQuery implements SqlDatabaseQuery<Collection<SettingWatcher>> {
  private static final String LOAD_QUERY = "SELECT * FROM playersettings_settings WHERE owner=?";
  private final UserManager userManager;
  private final SettingsManager settingsManager;
  private final Connection connection;
  private final Collection<UUID> uuids;

  public LoadUsersQuery(Connection connection, Collection<UUID> uuids) {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.userManager = plugin.getUserManager();
    this.settingsManager = plugin.getSettingsManager();
    this.connection = connection;
    this.uuids = uuids;
  }

  @Override
  public Collection<SettingWatcher> query() throws SQLException {
    Collection<SettingWatcher> settingWatchers = new ArrayList<>();
    for (UUID uuid : uuids) {
      settingWatchers.add(loadSettingWatcher(uuid));
    }
    return settingWatchers;
  }

  private SettingWatcher loadSettingWatcher(UUID uuid) throws SQLException {
    SettingWatcher settingWatcher = userManager.getSettingWatcher(uuid);
    try (ResultSet resultSet = executeQuery(uuid)) {
      while (resultSet.next()) {
        String settingName = resultSet.getString("settingName");
        Setting setting = settingsManager.getSetting(settingName);
        if (setting == null || !setting.isEnabled()) {
          continue;
        }

        settingWatcher.setValue(settingName, resultSet.getInt("value"), true);
      }
    }
    return settingWatcher;
  }

  private ResultSet executeQuery(UUID uuid) throws SQLException {
    PreparedStatement loadStatement = connection.prepareStatement(LOAD_QUERY);
    loadStatement.setString(1, uuid.toString());

    return loadStatement.executeQuery();
  }
}
