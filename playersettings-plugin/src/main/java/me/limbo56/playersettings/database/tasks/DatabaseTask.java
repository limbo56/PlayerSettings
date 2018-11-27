package me.limbo56.playersettings.database.tasks;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseTask {
    private Connection connection;

    public DatabaseTask(Connection connection) {
        this.connection = connection;
    }

    public abstract void run() throws SQLException;

    public Connection getConnection() {
        return connection;
    }
}
