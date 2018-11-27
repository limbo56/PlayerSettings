package me.limbo56.playersettings.database.tables;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.PluginLogger;
import me.limbo56.playersettings.utils.storage.CollectionStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class DatabaseTableStore implements CollectionStore<ITable> {
    private PlayerSettings plugin;
    private Collection<ITable> iTableCollection;

    public DatabaseTableStore(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        iTableCollection = new ArrayList<>();
        addToStore(new PlayersTable());
        addToStore(new SettingsTable());
        addToStore(new PlayerSettingsTable());
    }

    public void createTables() {
        PluginLogger.info("Creating tables");
        plugin.getDatabaseTableStore().getStored().forEach(iTable -> {
            try {
                plugin.getDatabaseManager().createTable(iTable.getTable());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void unregister() {
        iTableCollection.clear();
    }

    @Override
    public void addToStore(ITable iTable) {
        iTableCollection.add(iTable);
    }

    @Override
    public Collection<ITable> getStored() {
        return iTableCollection;
    }
}
