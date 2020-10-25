package me.limbo56.playersettings.database.tables;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.database.Table;
import me.limbo56.playersettings.utils.storage.CollectionStore;

import java.util.function.Supplier;

public class DatabaseTableStore extends CollectionStore<Supplier<Table>> {
    private final PlayerSettings plugin;

    public DatabaseTableStore(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        super.register();
        addToStore(new PlayerSettingsTable());
    }

    @Override
    public void unregister() {
        super.unregister();
        plugin.getDatabaseConnector().disconnect();
    }

    public void createTables() {
        plugin.debug("Creating tables");
        plugin.getDatabaseTableStore().getStored().forEach(iTableCreator ->
                plugin.getDatabaseManager().createTable(iTableCreator.get())
        );
    }
}
