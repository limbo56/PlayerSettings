package me.limbo56.playersettings.database.configuration;

import com.zaxxer.hikari.HikariConfig;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.Version;
import org.bukkit.configuration.ConfigurationSection;

public class SQLDatabaseConfiguration extends DatabaseConfiguration {
  public SQLDatabaseConfiguration(ConfigurationSection section) {
    super(section);
  }

  public HikariConfig getPoolConfiguration() {
    String host = section.getString("host");
    String database = section.getString("database");
    HikariConfig config = new HikariConfig();

    // Configure driver and credentials
    if (Version.getServerVersion().isOlderThan("1.12.2")) {
      config.setDriverClassName("com.mysql.jdbc.Driver");
    } else {
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");
    }
    config.setJdbcUrl(String.format("jdbc:mysql://%s/%s", host, database));
    config.setUsername(section.getString("username"));
    config.setPassword(section.getString("password"));

    // Configure connection pool
    config.setPoolName("playersettings-Hikari");
    ConfigurationSection poolSection = section.getConfigurationSection("pool");
    if (poolSection != null) {
      config.setMaximumPoolSize(poolSection.getInt("maximum-pool-size"));
      config.setMinimumIdle(poolSection.getInt("minimum-idle"));
      config.setMaxLifetime(poolSection.getInt("max-lifetime"));
      config.setKeepaliveTime(poolSection.getInt("keepalive-time"));
      config.setConnectionTimeout(poolSection.getInt("connection-timeout"));
      config.setInitializationFailTimeout(-1);
    } else {
      PluginLogger.warning(
          "Missing database pool configuration section! Functionality might be affected");
    }

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
    ConfigurationSection properties = section.getConfigurationSection("properties");
    config.addDataSourceProperty("useUnicode", properties.getBoolean("use-unicode"));
    config.addDataSourceProperty("characterEncoding", properties.getString("character-encoding"));
    config.addDataSourceProperty("useSSL", properties.getBoolean("use-ssl"));
    config.addDataSourceProperty(
        "verifyServerCertificate", properties.getBoolean("verify-server-certificate"));

    return config;
  }
}
