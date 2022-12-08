package me.limbo56.playersettings.database.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTableTask implements DatabaseTask {
  private static final String CREATE_TABLE =
      "CREATE TABLE IF NOT EXISTS playersettings_settings("
          + "owner VARCHAR(255) NOT NULL, "
          + "settingName VARCHAR(255) NOT NULL, "
          + "value INTEGER NOT NULL, "
          + "PRIMARY KEY (owner, settingName))";
  private final Connection connection;

  public CreateTableTask(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void execute() throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE);
    preparedStatement.execute();
  }
}
