package me.limbo56.playersettings.database.sql.query;

import java.sql.SQLException;

public interface SqlDatabaseQuery<T> {
  T query() throws SQLException;
}
