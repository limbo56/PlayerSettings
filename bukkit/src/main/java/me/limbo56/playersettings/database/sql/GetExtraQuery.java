package me.limbo56.playersettings.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import me.limbo56.playersettings.api.setting.Setting;

public class GetExtraQuery implements SqlDatabaseQuery<String> {
  private static final String LOAD_QUERY =
      "SELECT * FROM playersettings_extra WHERE owner=? AND settingName=? AND `key`=?";
  private final Connection connection;
  private final UUID uuid;
  private final Setting setting;
  private final String key;

  public GetExtraQuery(Connection connection, UUID uuid, Setting setting, String key) {
    this.connection = connection;
    this.uuid = uuid;
    this.setting = setting;
    this.key = key;
  }

  @Override
  public String query() throws SQLException {
    PreparedStatement loadStatement = connection.prepareStatement(LOAD_QUERY);
    loadStatement.setString(1, uuid.toString());
    loadStatement.setString(2, setting.getName());
    loadStatement.setString(3, key);

    ResultSet resultSet = loadStatement.executeQuery();
    return resultSet.next() ? resultSet.getString("value") : null;
  }
}
