package me.limbo56.playersettings.database.sql;

import java.sql.SQLException;

public interface SqlDatabaseQuery<T> {
  T query() throws SQLException;
}
