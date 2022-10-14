package me.limbo56.playersettings.database.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import me.limbo56.playersettings.PlayerSettings;

public class CreateTableTask extends DatabaseTask {
  private static final String CREATE_TABLE =
      "CREATE TABLE IF NOT EXISTS playersettings_settings("
          + "owner VARCHAR(255) NOT NULL, "
          + "settingName VARCHAR(255) NOT NULL, "
          + "value INTEGER NOT NULL, "
          + "PRIMARY KEY (owner, settingName))";

  public CreateTableTask(PlayerSettings plugin, Connection connection) {
    super(plugin, connection);
  }

  @Override
  public void run() {
    try (Connection connection = this.connection) {
      PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE);
      preparedStatement.execute();
    } catch (SQLException e) {
      plugin.getLogger().severe("Failed while creating table 'settings'");
      e.printStackTrace();
    }
  }
}
