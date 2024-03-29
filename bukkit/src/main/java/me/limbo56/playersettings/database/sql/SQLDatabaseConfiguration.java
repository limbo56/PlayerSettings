package me.limbo56.playersettings.database.sql;

import com.zaxxer.hikari.HikariConfig;
import me.limbo56.playersettings.database.DatabaseConfiguration;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;

public class SQLDatabaseConfiguration extends DatabaseConfiguration {
  public SQLDatabaseConfiguration(ConfigurationSection section) {
    super(section);
  }

  public HikariConfig getPoolConfiguration() {
    HikariConfig config = new HikariConfig();

    configureCredentials(config);
    configureDriver(config);
    configurePool(config);
    configureRecommendedProperties(config);
    configureExtraProperties(config);

    return config;
  }

  private void configureCredentials(HikariConfig config) {
    config.setUsername(configuration.getString("username"));
    config.setPassword(configuration.getString("password"));
  }

  private void configureDriver(HikariConfig config) {
    String host = configuration.getString("host");
    String database = configuration.getString("database");
    String type = configuration.getString("type", "sqlite");

    if (type.equalsIgnoreCase("sql")) {
      String driverClassName;
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        driverClassName = "com.mysql.cj.jdbc.Driver";
      } catch (ClassNotFoundException e) {
        driverClassName = "com.mysql.jdbc.Driver";
      }

      config.setDriverClassName(driverClassName);
      config.setJdbcUrl(String.format("jdbc:mysql://%s/%s", host, database));
    } else {
      config.setDriverClassName("org.sqlite.JDBC");
      config.setConnectionTestQuery("SELECT 1");
      config.setJdbcUrl(String.format("jdbc:sqlite:plugins/PlayerSettings/%s.db", database));
    }
  }

  private void configurePool(HikariConfig config) {
    ConfigurationSection poolSection = configuration.getConfigurationSection("pool");
    config.setPoolName("PlayerSettings-Hikari");

    if (poolSection != null) {
      config.setMaximumPoolSize(poolSection.getInt("maximum-pool-size", 10));
      config.setMinimumIdle(poolSection.getInt("minimum-idle", 10));
      config.setMaxLifetime(poolSection.getInt("max-lifetime", 1800000));
      config.setKeepaliveTime(poolSection.getInt("keep-alive-time", 0));
      config.setConnectionTimeout(poolSection.getInt("connection-timeout", 5000));
      config.setInitializationFailTimeout(-1);
    } else {
      PluginLogger.warning(
          "Missing database pool configuration section! Functionality might be affected");
    }
  }

  private void configureRecommendedProperties(HikariConfig config) {
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
  }

  private void configureExtraProperties(HikariConfig config) {
    ConfigurationSection properties = configuration.getConfigurationSection("properties");
    if (properties == null) {
      PluginLogger.warning("Missing 'properties' section from sql database configuration");
      return;
    }

    config.addDataSourceProperty("useUnicode", properties.getBoolean("use-unicode", true));
    config.addDataSourceProperty(
        "characterEncoding", properties.getString("character-encoding", "utf8"));
    config.addDataSourceProperty("useSSL", properties.getBoolean("use-ssl", false));
    config.addDataSourceProperty(
        "verifyServerCertificate", properties.getBoolean("verify-server-certificate", false));
  }
}
