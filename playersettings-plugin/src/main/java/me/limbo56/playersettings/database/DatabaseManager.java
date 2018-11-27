package me.limbo56.playersettings.database;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.database.tasks.*;
import me.limbo56.playersettings.player.SPlayer;
import me.limbo56.playersettings.utils.database.DatabaseTable;

import java.sql.SQLException;
import java.util.function.Consumer;

public class DatabaseManager {
    private PlayerSettings plugin;

    public DatabaseManager(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    public void createTable(DatabaseTable databaseTable) throws SQLException {
        if (plugin.getConfiguration().getBoolean("Mysql.enable"))
            new CreateTableTask(plugin.getDatabaseConnector().getConnection(), databaseTable).run();
    }

    public void addSetting(Setting setting) throws SQLException {
        if (plugin.getConfiguration().getBoolean("Mysql.enable"))
            new AddSettingTask(plugin.getDatabaseConnector().getConnection(), setting).run();
    }

    public void addPlayer(SPlayer sPlayer) throws SQLException {
        if (plugin.getConfiguration().getBoolean("Mysql.enable"))
            new AddPlayerTask(plugin.getDatabaseConnector().getConnection(), sPlayer).run();
    }

    public void loadPlayer(SPlayer sPlayer, Consumer<SPlayer> sPlayerSupplier) throws SQLException {
        if (plugin.getConfiguration().getBoolean("Mysql.enable"))
            new LoadPlayerTask(plugin.getDatabaseConnector().getConnection(), sPlayer).run();
        sPlayerSupplier.accept(sPlayer);
    }

    public void savePlayer(SPlayer sPlayer, Consumer<SPlayer> sPlayerSupplier) throws SQLException {
        if (plugin.getConfiguration().getBoolean("Mysql.enable"))
            new SavePlayerTask(plugin.getDatabaseConnector().getConnection(), sPlayer).run();
        sPlayerSupplier.accept(sPlayer);
    }
}
