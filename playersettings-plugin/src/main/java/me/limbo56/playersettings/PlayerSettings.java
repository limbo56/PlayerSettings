package me.limbo56.playersettings;

import me.limbo56.playersettings.api.PlayerSettingsAPI;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingUpdateEvent;
import me.limbo56.playersettings.command.CommandStore;
import me.limbo56.playersettings.configuration.ConfigurationStore;
import me.limbo56.playersettings.configuration.YmlConfiguration;
import me.limbo56.playersettings.database.DatabaseConnector;
import me.limbo56.playersettings.database.DatabaseManager;
import me.limbo56.playersettings.database.tables.DatabaseTableStore;
import me.limbo56.playersettings.database.tables.ITable;
import me.limbo56.playersettings.listeners.ListenerStore;
import me.limbo56.playersettings.listeners.MessageReceiveListener;
import me.limbo56.playersettings.menu.SettingStore;
import me.limbo56.playersettings.player.SPlayer;
import me.limbo56.playersettings.player.SPlayerStore;
import me.limbo56.playersettings.utils.PluginLogger;
import me.limbo56.playersettings.utils.storage.CollectionStore;
import me.limbo56.playersettings.utils.storage.MapStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerSettings extends JavaPlugin implements PlayerSettingsAPI {
    private ConfigurationStore configurationStore;
    private SPlayerStore sPlayerStore;
    private CommandStore commandStore;
    private SettingStore settingStore;
    private DatabaseTableStore databaseTableStore;
    private ListenerStore listenerStore;

    private DatabaseConnector databaseConnector;
    private DatabaseManager databaseManager;

    public static PlayerSettings getPlugin() {
        return getPlugin(PlayerSettings.class);
    }

    @Override
    public void onEnable() {
        // Initialize
        configurationStore = new ConfigurationStore();
        sPlayerStore = new SPlayerStore();
        commandStore = new CommandStore();
        settingStore = new SettingStore(this);
        databaseTableStore = new DatabaseTableStore(this);
        listenerStore = new ListenerStore(this);
        databaseConnector = new DatabaseConnector(this);
        databaseManager = new DatabaseManager(this);

        // Register stores
        configurationStore.register();
        sPlayerStore.register();
        commandStore.register();
        settingStore.register();
        databaseTableStore.register();
        listenerStore.register();

        // Register plugin channel
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageReceiveListener(this));

        // Connect to database and create tables
        if (getConfiguration().getBoolean("Mysql.enable")) {
            databaseConnector.connect();
            databaseTableStore.createTables();
        }
    }

    @Override
    public void onDisable() {
        // Unregister stores
        sPlayerStore.unregister();
        commandStore.unregister();
        settingStore.unregister();
        databaseTableStore.unregister();
        listenerStore.unregister();
        configurationStore.unregister();

        // Disconnect from database
        databaseConnector.disconnect();
    }

    @Override
    public void registerSetting(Setting setting) {
        getSettingStore().addToStore(setting.getRawName(), setting);
    }

    @Override
    public void callSettingUpdate(Player player, Setting setting) {
        Bukkit.getPluginManager().callEvent(new SettingUpdateEvent(player, setting));
    }

    public YmlConfiguration getConfiguration() {
        return getConfigurationStore().getStored().get("config");
    }

    public MapStore<UUID, SPlayer> getsPlayerStore() {
        return sPlayerStore;
    }

    public MapStore<String, YmlConfiguration> getConfigurationStore() {
        return configurationStore;
    }

    public MapStore<String, Setting> getSettingStore() {
        return settingStore;
    }

    public CollectionStore<ITable> getDatabaseTableStore() {
        return databaseTableStore;
    }

    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
