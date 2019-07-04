package me.limbo56.playersettings.database;

import lombok.RequiredArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.database.tasks.CreateTableTask;
import me.limbo56.playersettings.database.tasks.LoadPlayerTask;
import me.limbo56.playersettings.database.tasks.SavePlayerTask;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.utils.database.Table;
import org.bukkit.Bukkit;

import java.sql.SQLException;

@RequiredArgsConstructor
public class DatabaseManager {
    private final PlayerSettings plugin;

    public void createTable(Table table) {
        try {
            Bukkit.getScheduler().runTask(plugin, new CreateTableTask(plugin, plugin.getDatabaseConnector().getConnection(), table));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayer(SPlayer sPlayer) {
        try {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new LoadPlayerTask(plugin, plugin.getDatabaseConnector().getConnection(), sPlayer));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(SPlayer sPlayer, boolean async) {
        try {
            Runnable task = new SavePlayerTask(plugin, plugin.getDatabaseConnector().getConnection(), sPlayer);
            if (async) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
                return;
            }

            synchronized(this) {
                task.run();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
