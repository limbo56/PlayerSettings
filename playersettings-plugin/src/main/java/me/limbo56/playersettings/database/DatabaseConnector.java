package me.limbo56.playersettings.database;

import com.zaxxer.hikari.HikariDataSource;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.YmlConfiguration;
import me.limbo56.playersettings.utils.PluginLogger;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {
    private PlayerSettings plugin;
    private HikariDataSource hikariDataSource;

    public DatabaseConnector(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        if (plugin.getConfiguration().getBoolean("Mysql.enable")) {
            PluginLogger.info("Connecting to database");
            YmlConfiguration config = plugin.getConfiguration();

            // Initialize data source
            hikariDataSource = new HikariDataSource();
            hikariDataSource.setPoolName("PlayerSettings");
            hikariDataSource.setMaximumPoolSize(10);
            hikariDataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikariDataSource.addDataSourceProperty("serverName", config.getString("Mysql.host"));
            hikariDataSource.addDataSourceProperty("port", config.getInt("Mysql.port"));
            hikariDataSource.addDataSourceProperty("databaseName", config.getString("Mysql.database"));
            hikariDataSource.addDataSourceProperty("user", config.getString("Mysql.user"));
            hikariDataSource.addDataSourceProperty("password", config.getString("Mysql.password"));
        }
    }

    public void disconnect() {
        if (hikariDataSource != null && hikariDataSource.isRunning()) {
            PluginLogger.info("Disconnecting from database");
            hikariDataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
