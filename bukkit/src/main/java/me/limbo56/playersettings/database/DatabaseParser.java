package me.limbo56.playersettings.database;

import com.zaxxer.hikari.HikariConfig;
import me.limbo56.playersettings.util.data.Parser;
import org.bukkit.configuration.ConfigurationSection;

public class DatabaseParser implements Parser<ConfigurationSection, HikariConfig> {
  public static final DatabaseParser DATABASE_PARSER = new DatabaseParser();

  @Override
  public HikariConfig parse(ConfigurationSection databaseSection) {
    String host = databaseSection.getString("host");
    String database = databaseSection.getString("database");
    HikariConfig config = new HikariConfig();

    // Configure driver and credentials
    config.setDriverClassName("com.mysql.cj.jdbc.Driver");
    config.setJdbcUrl(String.format("jdbc:mysql://%s/%s", host, database));
    config.setUsername(databaseSection.getString("username"));
    config.setPassword(databaseSection.getString("password"));

    // Configure connection pool
    ConfigurationSection pool = databaseSection.getConfigurationSection("pool");
    config.setPoolName("playersettings-Hikari");
    config.setMaximumPoolSize(pool.getInt("maximum-pool-size"));
    config.setMinimumIdle(pool.getInt("minimum-idle"));
    config.setMaxLifetime(pool.getInt("max-lifetime"));
    config.setKeepaliveTime(pool.getInt("keepalive-time"));
    config.setConnectionTimeout(pool.getInt("connection-timeout"));
    config.setInitializationFailTimeout(-1);

    // Add MySQL recommended properties
    // https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    config.addDataSourceProperty("useServerPrepStmts", "true");
    config.addDataSourceProperty("useLocalSessionState", "true");
    config.addDataSourceProperty("rewriteBatchedStatements", "true");
    config.addDataSourceProperty("cacheResultSetMetadata", "true");
    config.addDataSourceProperty("cacheServerConfiguration", "true");
    config.addDataSourceProperty("elideSetAutoCommits", "true");
    config.addDataSourceProperty("maintainTimeStats", "false");

    // Extra properties
    ConfigurationSection properties = databaseSection.getConfigurationSection("properties");
    config.addDataSourceProperty("useUnicode", properties.getBoolean("use-unicode"));
    config.addDataSourceProperty("characterEncoding", properties.getString("character-encoding"));
    config.addDataSourceProperty("useSSL", properties.getBoolean("use-ssl"));
    config.addDataSourceProperty(
        "verifyServerCertificate", properties.getBoolean("verify-server-certificate"));
    return config;
  }
}
