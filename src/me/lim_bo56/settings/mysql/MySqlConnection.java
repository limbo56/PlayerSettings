package me.lim_bo56.settings.mysql;

import me.lim_bo56.settings.Core;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by lim_bo56
 * On 8/11/2016
 * At 8:05 PM
 */
public class MySqlConnection {

    private static MySqlConnection instance = new MySqlConnection();

    private Connection connection;

    private String host = Core.getInstance().getConfig().getString("MySQL.host");
    private String port = Core.getInstance().getConfig().getString("MySQL.port");
    private String database = Core.getInstance().getConfig().getString("MySQL.database");
    private String name = Core.getInstance().getConfig().getString("MySQL.name");
    private String password = Core.getInstance().getConfig().getString("MySQL.password");

    public static MySqlConnection getInstance() {
        return instance;
    }

    public void openConnection() {

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, name, password);
        } catch (SQLException exception) {
            Bukkit.getLogger().severe("Couldn't connect to the database.");
            Bukkit.getLogger().severe("Please, check config.yml for any errors on the database info!");
        }

    }

    public void checkTable() {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        getCurrentConnection().createStatement()
                                .execute("CREATE TABLE IF NOT EXISTS `players` ("
                                        + "`UUID` varchar(36) NOT NULL,"
                                        + "`Visibility` TINYINT(0) DEFAULT NULL,"
                                        + "`Stacker` TINYINT(0) DEFAULT NULL,"
                                        + "`Chat` TINYINT(0) DEFAULT NULL,"
                                        + "`Vanish` TINYINT(0) DEFAULT NULL,"
                                        + "`Fly` TINYINT(0) DEFAULT NULL,"
                                        + "`Speed` TINYINT(0) DEFAULT NULL,"
                                        + "`Jump` TINYINT(0) DEFAULT NULL, PRIMARY KEY (UUID))");
                    }

                } catch (SQLException e) {
                    Bukkit.getLogger().severe("Couldn't create tables on the database ;(.");
                }
            }
        });
    }

    public Connection getCurrentConnection() {
        return connection;
    }
}
