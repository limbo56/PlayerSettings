package me.limbo56.playersettings.database.sql;

import com.google.common.collect.ImmutableMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import me.limbo56.playersettings.api.setting.Setting;

public class SaveExtraTask implements SqlDatabaseTask {
  private static final Map<String, String> STATEMENT_MAP =
      ImmutableMap.of(
          "sql",
          "INSERT INTO playersettings_extra (owner, settingName, `key`, value) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value)",
          "sqlite",
          "INSERT OR REPLACE INTO playersettings_extra (owner, settingName, `key`, value) VALUES (?, ?, ?, ?)");
  private final Connection connection;
  private final UUID uuid;
  private final Setting setting;
  private final String key;
  private final String value;
  private final String query;

  public SaveExtraTask(
      Connection connection,
      UUID uuid,
      Setting setting,
      String key,
      String value,
      String statementType) {
    this.connection = connection;
    this.uuid = uuid;
    this.setting = setting;
    this.key = key;
    this.value = value;
    this.query = STATEMENT_MAP.get(statementType);
  }

  @Override
  public void execute() throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, uuid.toString());
    preparedStatement.setString(2, setting.getName());
    preparedStatement.setString(3, key);
    preparedStatement.setString(4, value);
    preparedStatement.execute();
  }
}
