package me.limbo56.playersettings.database.configuration;

import com.zaxxer.hikari.HikariConfig;
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
    config.setUsername(section.getString("username"));
    config.setPassword(section.getString("password"));
  }

  private void configureDriver(HikariConfig config) {
    String host = section.getString("host");
    String database = section.getString("database");
    String type = section.getString("type", "sqlite");

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
    ConfigurationSection poolSection = section.getConfigurationSection("pool");
    config.setPoolName("PlayerSettings-Hikari");

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
    ConfigurationSection properties = section.getConfigurationSection("properties");
    if (properties == null) {
      PluginLogger.warning("Missing 'properties' section from sql database configuration");
      return;
    }

    config.addDataSourceProperty("useUnicode", properties.getBoolean("use-unicode"));
    config.addDataSourceProperty("characterEncoding", properties.getString("character-encoding"));
    config.addDataSourceProperty("useSSL", properties.getBoolean("use-ssl"));
    config.addDataSourceProperty(
        "verifyServerCertificate", properties.getBoolean("verify-server-certificate"));
  }
}
