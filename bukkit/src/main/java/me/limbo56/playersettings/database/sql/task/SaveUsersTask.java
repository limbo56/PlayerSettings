package me.limbo56.playersettings.database.sql.task;

import com.google.common.collect.ImmutableMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import me.limbo56.playersettings.api.SettingWatcher;

public class SaveUsersTask implements SqlDatabaseTask {
  private static final Map<String, String> STATEMENT_MAP =
      ImmutableMap.of(
          "sql",
          "INSERT INTO playersettings_settings (owner, settingName, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value)",
          "sqlite",
          "INSERT OR REPLACE INTO playersettings_settings (owner, settingName, value) VALUES (?, ?, ?)");
  private final Connection connection;
  private final Collection<SettingWatcher> settings;
  private final String query;

  public SaveUsersTask(
      Connection connection, Collection<SettingWatcher> settings, String statementType) {
    this.connection = connection;
    this.query = STATEMENT_MAP.get(statementType);
    this.settings = settings;
  }

  @Override
  public void execute() throws SQLException {
    PreparedStatement statement = connection.prepareStatement(query);
    connection.setAutoCommit(false);

    for (SettingWatcher settingWatcher : settings) {
      for (String settingName : settingWatcher.getWatched()) {
        statement.setString(1, settingWatcher.getOwner().toString());
        statement.setString(2, settingName);
        statement.setInt(3, settingWatcher.getValue(settingName));
        statement.addBatch();
      }
    }

    statement.executeBatch();
    connection.commit();
  }
}
