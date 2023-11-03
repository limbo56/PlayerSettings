package me.limbo56.playersettings.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTableTask implements SqlDatabaseTask {
  private static final String SETTINGS_TABLE =
      "CREATE TABLE IF NOT EXISTS playersettings_settings("
          + "owner VARCHAR(255) NOT NULL, "
          + "settingName VARCHAR(255) NOT NULL, "
          + "value INTEGER NOT NULL, "
          + "PRIMARY KEY (owner, settingName))";
  private static final String EXTRA_TABLE =
      "CREATE TABLE IF NOT EXISTS playersettings_extra("
          + "owner VARCHAR(255) NOT NULL, "
          + "settingName VARCHAR(255) NOT NULL, "
          + "`key` VARCHAR(255) NOT NULL, "
          + "value INTEGER NOT NULL, "
          + "PRIMARY KEY (owner, settingName, `key`))";
  private final Connection connection;

  public CreateTableTask(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void execute() throws SQLException {
    PreparedStatement settingsTableStatement = connection.prepareStatement(SETTINGS_TABLE);
    PreparedStatement extraTableStatement = connection.prepareStatement(EXTRA_TABLE);
    settingsTableStatement.execute();
    extraTableStatement.execute();
  }
}
