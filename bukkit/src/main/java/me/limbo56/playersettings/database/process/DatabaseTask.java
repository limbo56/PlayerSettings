package me.limbo56.playersettings.database.process;

import java.sql.SQLException;

public interface DatabaseTask {
  void execute() throws SQLException;
}
