package me.limbo56.playersettings;

import lombok.Getter;
import me.limbo56.playersettings.api.PlayerSettingsApi;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.command.CommandStore;
import me.limbo56.playersettings.configuration.ConfigurationStore;
import me.limbo56.playersettings.configuration.YmlConfiguration;
import me.limbo56.playersettings.database.DatabaseConnector;
import me.limbo56.playersettings.database.DatabaseManager;
import me.limbo56.playersettings.database.tables.DatabaseTableStore;
import me.limbo56.playersettings.listeners.ListenerStore;
import me.limbo56.playersettings.menu.SettingsHolder;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.settings.SettingStore;
import me.limbo56.playersettings.utils.PlayerUtils;
import me.limbo56.playersettings.utils.PluginUpdater;
import me.limbo56.playersettings.utils.storage.MapStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Getter
public class PlayerSettings extends JavaPlugin implements PlayerSettingsApi {

    // Stores
    private ConfigurationStore configurationStore;
    private MapStore<UUID, SPlayer> sPlayerStore;
    private CommandStore commandStore;
    private DatabaseTableStore databaseTableStore;
    private ListenerStore listenerStore;
    private SettingStore settingStore;

    // Database
    private DatabaseConnector databaseConnector;
    private DatabaseManager databaseManager;

    private boolean reloading = false;

    public static PlayerSettings getPlugin() {
        return getPlugin(PlayerSettings.class);
    }

    @Override
    public void onEnable() {
        // Initialize
        configurationStore = new ConfigurationStore(this);
        sPlayerStore = new MapStore<>();
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

        // Connect to database and create tables
        if (getConfiguration().getBoolean("Database.enabled")) {
            databaseConnector.connect();
            databaseTableStore.createTables();
        }

        // Send update message to console
        PluginUpdater.logUpdateMessage();

        // Load online players after all plugins load
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () ->
                Bukkit.getOnlinePlayers().forEach(PlayerUtils::loadPlayer)
        );
    }

    @Override
    public void onDisable() {
        // Unload players
        for (SPlayer sPlayer : sPlayerStore.getStored().values()) {
            sPlayer.unloadPlayer(false);
        }

        // Unregister stores
        sPlayerStore.unregister();
        commandStore.unregister();
        settingStore.unregister();
        listenerStore.unregister();
        databaseTableStore.unregister();
        configurationStore.unregister();
    }

    public void debug(String message) {
        if (!getConfiguration().getBoolean("debug")) {
            return;
        }

        getLogger().info("[DEBUG] " + message);
    }

    @Override
    public Setting getSetting(String rawName) {
        return settingStore.getStored().get(rawName);
    }

    @Override
    public void registerSetting(Setting setting) {
        settingStore.addToStore(setting.getRawName(), setting);
    }

    @Override
    public void registerCallback(Setting setting, SettingCallback settingCallback) {
        settingStore.addCallback(setting, settingCallback);
    }

    public void setReloading(boolean reloading) {
        // Close settings inventories
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getOpenInventory().getTopInventory().getHolder() instanceof SettingsHolder)
                .forEach(HumanEntity::closeInventory);

        // Set reloading
        this.reloading = reloading;
    }

    public SPlayer getSPlayer(UUID uuid) {
        return getSPlayerStore().getStored().get(uuid);
    }

    public YmlConfiguration getConfiguration() {
        return getConfigurationStore().getStored().get("config");
    }

    public YmlConfiguration getConfiguration(String name) {
        return getConfigurationStore().getStored().get(name);
    }
}
