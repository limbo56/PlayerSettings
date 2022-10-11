package me.limbo56.playersettings.database.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.user.SettingUser;

public class SaveUsersTask extends DatabaseTask {
  private static final String SAVE_PLAYER_QUERY =
      "INSERT INTO playersettings_settings (owner, settingName, value) "
          + "VALUES (?, ?, ?) "
          + "ON DUPLICATE KEY UPDATE value = VALUES(value)";
  private static final String SAVE_PLAYER_QUERY_SQLITE =
      "INSERT OR REPLACE INTO playersettings_settings (owner, settingName, value) VALUES (?, ?, ?)";
  private final Collection<SettingUser> players;
  private final String query;

  public SaveUsersTask(
      PlayerSettings plugin, Connection connection, Collection<SettingUser> players) {
    this(plugin, connection, players, "");
  }

  public SaveUsersTask(
      PlayerSettings plugin, Connection connection, Collection<SettingUser> players, String type) {
    super(plugin, connection);
    this.players = players;
    this.query = type.equals("sqlite") ? SAVE_PLAYER_QUERY_SQLITE : SAVE_PLAYER_QUERY;
  }

  @Override
  public void run() {
    try (Connection connection = this.connection) {
      PreparedStatement statement = connection.prepareStatement(query);
      connection.setAutoCommit(false);

      for (SettingUser player : players) {
        SettingWatcher settingWatcher = player.getSettingWatcher();
        for (String settingName : settingWatcher.getWatched()) {
          statement.setString(1, player.getUniqueId().toString());
          statement.setString(2, settingName);
          statement.setInt(3, settingWatcher.getValue(settingName));
          statement.addBatch();
        }
      }

      statement.executeBatch();
      connection.commit();
    } catch (SQLException e) {
      plugin.getLogger().severe("Failed to save players");
      e.printStackTrace();
    }
  }
}
