package me.limbo56.playersettings.database.sql.task;

import java.sql.SQLException;

public interface SqlDatabaseTask {
  void execute() throws SQLException;
}
