package me.limbo56.playersettings.database.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.user.SettingUser;

public class LoadUsersTask extends DatabaseTask {
  private static final String LOAD_QUERY = "SELECT * FROM playersettings_settings WHERE owner=?";
  private final Collection<SettingUser> playerList;

  public LoadUsersTask(
      PlayerSettings plugin, Connection connection, Collection<SettingUser> playerList) {
    super(plugin, connection);
    this.playerList = playerList;
  }

  @Override
  public void run() {
    try (Connection connection = this.connection) {
      for (SettingUser user : playerList) {
        PreparedStatement loadStatement = connection.prepareStatement(LOAD_QUERY);
        loadStatement.setString(1, user.getUniqueId().toString());

        ResultSet resultSet = loadStatement.executeQuery();
        while (resultSet.next()) {
          String settingName = resultSet.getString("settingName");
          int value = resultSet.getInt("value");
          loadSetting(user, settingName, value);
        }
      }
      playerList.forEach(settingsPlayer -> settingsPlayer.setLoading(false));
    } catch (SQLException e) {
      plugin.getLogger().severe("Failed to load settings for online players");
      e.printStackTrace();
    }
  }

  private void loadSetting(SettingUser user, String settingName, int value) {
    if (!plugin.getSettingsContainer().isSettingLoaded(settingName)) {
      return;
    }

    Setting setting = plugin.getSettingsContainer().getSetting(settingName);
    int maxValue = PlayerSettingsProvider.getSettingPermissionLevel(user.getPlayer(), setting);
    int safeValue = Math.abs(value) > maxValue ? setting.getDefaultValue() : value;
    plugin
        .getUserManager()
        .getSettingWatcher(user.getUniqueId())
        .setValue(settingName, safeValue, !setting.shouldExecuteOnJoin());
  }
}
