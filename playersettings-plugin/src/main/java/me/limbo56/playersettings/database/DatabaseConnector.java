package me.limbo56.playersettings.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.YmlConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DatabaseConnector {
    private final PlayerSettings plugin;
    private HikariDataSource hikariDataSource;

    public void connect() {
        plugin.debug("Connecting to database");

        YmlConfiguration config = plugin.getConfiguration();
        String host = config.getString("Database.host");
        int port = config.getInt("Database.port");
        String databaseName = config.getString("Database.name");
        String user = config.getString("Database.user");
        String password = config.getString("Database.password");

        // Initialize data source
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariDataSource.addDataSourceProperty("serverName", host);
        hikariDataSource.addDataSourceProperty("port", port);
        hikariDataSource.addDataSourceProperty("databaseName", databaseName);
        hikariDataSource.addDataSourceProperty("user", user);
        hikariDataSource.addDataSourceProperty("password", password);
        hikariDataSource.addDataSourceProperty("autoReconnect", true);
        hikariDataSource.addDataSourceProperty("useSSL", false);
        hikariDataSource.addDataSourceProperty("serverTimezone", "UTC");
    }

    public void disconnect() {
        if (hikariDataSource != null && hikariDataSource.isRunning()) {
            plugin.debug("Disconnecting from database");
            hikariDataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
