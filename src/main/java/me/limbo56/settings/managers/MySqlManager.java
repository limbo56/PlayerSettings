package me.limbo56.settings.managers;

import me.limbo56.settings.PlayerSettings;
import org.bukkit.Bukkit;

import java.sql.*;

/**
 * Created by lim_bo56
 * On 8/11/2016
 * At 8:05 PM
 */
public class MySqlManager {

    private Connection connection;

    private String host = PlayerSettings.getInstance().getConfig().getString("MySQL.host");
    private String port = PlayerSettings.getInstance().getConfig().getString("MySQL.port");
    private String database = PlayerSettings.getInstance().getConfig().getString("MySQL.database");
    private String name = PlayerSettings.getInstance().getConfig().getString("MySQL.name");
    private String password = PlayerSettings.getInstance().getConfig().getString("MySQL.password");

    public void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", name, password);

            PlayerSettings.getInstance().log("Connected to database");
        } catch (SQLException exception) {
            Bukkit.getLogger().severe("Couldn't connect to the database.");
            Bukkit.getLogger().severe("Please, check config.yml for any errors on the database info!");
        }
    }

    public boolean checkTable() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "PlayerSettings", null);

            return tables.next();
        } catch (SQLException exception) {
            return false;
        }
    }

    public void createTable() {
        Bukkit.getScheduler().runTaskAsynchronously(PlayerSettings.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        getCurrentConnection().createStatement()
                                .execute("CREATE TABLE IF NOT EXISTS `PlayerSettings` ("
                                        + "`UUID` varchar(36) NOT NULL,"
                                        + "`Visibility` TINYINT(0) DEFAULT NULL,"
                                        + "`Stacker` TINYINT(0) DEFAULT NULL,"
                                        + "`Chat` TINYINT(0) DEFAULT NULL,"
                                        + "`Vanish` TINYINT(0) DEFAULT NULL,"
                                        + "`Fly` TINYINT(0) DEFAULT NULL,"
                                        + "`Speed` TINYINT(0) DEFAULT NULL,"
                                        + "`Jump` TINYINT(0) DEFAULT NULL,"
                                        + "`Radio` TINYINT(0) DEFAULT NULL,"
                                        + "`DoubleJump` TINYINT(0) DEFAULT NULL, PRIMARY KEY (UUID))");

                        if (ConfigurationManager.getConfig().getBoolean("Debug"))
                            PlayerSettings.getInstance().log("createTable: Table created or already existent");

                        ResultSetMetaData table = getCurrentConnection().createStatement().executeQuery("SELECT * FROM `PlayerSettings`").getMetaData();

                        boolean radio = false;
                        boolean doubleJump = false;

                        for (int i = 1; i <= table.getColumnCount(); i++) {
                            if ("Radio".equals(table.getColumnName(i))) {
                                if (ConfigurationManager.getConfig().getBoolean("Debug"))
                                    PlayerSettings.getInstance().log("createTable: Radio column found, doing nothing");

                                radio = true;
                            } else if ("DoubleJump".equals(table.getColumnName(i))) {
                                if (ConfigurationManager.getConfig().getBoolean("Debug"))
                                    PlayerSettings.getInstance().log("createTable: DoubleJump column found, doing nothing");

                                doubleJump = true;
                            }
                            if (radio && doubleJump)
                                break;
                        }

                        if (!radio) {
                            getCurrentConnection().createStatement().executeUpdate("ALTER TABLE `PlayerSettings` ADD `Radio` TINYINT(0) DEFAULT NULL");

                            if (ConfigurationManager.getConfig().getBoolean("Debug"))
                                PlayerSettings.getInstance().log("createTable: Radio column created successfully");
                        }

                        if (!doubleJump) {
                            getCurrentConnection().createStatement().executeUpdate("ALTER TABLE `PlayerSettings` ADD `DoubleJump` TINYINT(0) DEFAULT NULL");

                            if (ConfigurationManager.getConfig().getBoolean("Debug"))
                                PlayerSettings.getInstance().log("createTable: DoubleJump column created successfully");
                        }
                    }
                } catch (SQLException e) {
                    Bukkit.getLogger().severe("Couldn't create tables on the database :(");
                }
            }
        });
    }

    public Connection getCurrentConnection() {
        return connection;
    }
}
