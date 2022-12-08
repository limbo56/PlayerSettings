package me.limbo56.playersettings.database.process;

import java.sql.SQLException;

public interface DatabaseQuery<T> {
  T query() throws SQLException;
}
