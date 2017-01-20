package me.lim_bo56.settings;

import me.lim_bo56.settings.config.DefaultConfiguration;
import me.lim_bo56.settings.config.MenuConfiguration;
import me.lim_bo56.settings.config.MessageConfiguration;
import me.lim_bo56.settings.listeners.FlyToggleListener;
import me.lim_bo56.settings.listeners.PlayerListener;
import me.lim_bo56.settings.listeners.WorldListener;
import me.lim_bo56.settings.managers.CommandManager;
import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.managers.VersionManager;
import me.lim_bo56.settings.menus.SettingsMenu;
import me.lim_bo56.settings.mysql.MySqlConnection;
import me.lim_bo56.settings.utils.Updater;
import me.lim_bo56.settings.utils.Utilities;
import me.lim_bo56.settings.version.IItemGlower;
import me.lim_bo56.settings.version.IMount;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:36 AM
 */
public class Core extends JavaPlugin {

    private static Core instance;

    public SortedMap<Integer, String> commandHelp = new TreeMap<>();

    private VersionManager versionManager;

    private PluginManager pm = Bukkit.getServer().getPluginManager();

    /**
     * Method to log a string on console.
     *
     * @param object Message.
     */
    public void log(String object) {
        System.out.println("Preferences >> " + object);
    }

    public static Core getInstance() {
        return instance;
    }

    /**
     * Method called when plugin is enabled.
     */
    public void onEnable() {

        instance = this;

        String updater = Updater.consoleUpdater();

        String version = "1.8.8";

        try {

            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            log("This server version is not supported!");
        }

        log("======================================================");
        log("PlayerSettings v" + getDescription().getVersion() + " is being loaded...");

        log("");

        if (getConfig().getBoolean("MySQL.enable")) {
            log("Connecting to database...");

            MySqlConnection.getInstance().openConnection();
            MySqlConnection.getInstance().createTable();
            MySqlConnection.getInstance().updateTable();

            log("");
        }

        log("Loading all data...");

        loadDefaults(version);

        log("All data has been loaded");
        log("");

        log("PlayerSettings successfully finished loading!");
        log("Your server is running version " + version);
        log("======================================================");
        log("");

        log("Checking for updates...");
        log("");
        log(updater);
        log("Download: http://bit.ly/PlayerSettings");
        log("");

        Updater.consoleUpdater();

        Utilities.loadOnlinePlayers();

    }

    private void setupConfig() {

        File file = new File(getDataFolder(), "config.yml");
        boolean created;

        if (!file.exists()) {
            created = file.getParentFile().mkdir();
            log("Config file doesn't exist yet.");
            log("Creating Config File and loading it.");
            if (created) {
                log("File config.yml created with success!");
                log("");
            }
        }

        ConfigurationManager config = ConfigurationManager.getConfig();

        config.addDefault("MySQL.enable", false);
        config.addDefault("MySQL.host", "localhost");
        config.addDefault("MySQL.port", 3306);
        config.addDefault("MySQL.database", "playersettings");
        config.addDefault("MySQL.name", "root");
        config.addDefault("MySQL.password", "");

        config.addDefault("Update-Message", true);
        config.addDefault("Using-Citizens", false);
        config.saveConfig();
    }

    private void loadDefaults(String version) {

        // Setup config.yml
        setupConfig();

        // Get server version.
        versionManager = new VersionManager(version);
        versionManager.load();

        // Load Menu Defaults

        new MenuConfiguration();

        // Load messages.
        new MessageConfiguration();

        // Load data
        new DefaultConfiguration();

        // Register listeners.
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new FlyToggleListener(), this);
        pm.registerEvents(new WorldListener(), this);
        pm.registerEvents(new SettingsMenu(), this);

        // Creating help messages
        commandHelp.put(1, "§a/settings open §7- §cOpen the settings menu.");
        commandHelp.put(2, "§a/settings reload §7- §cReloads all config files.");

        // Register commands.
        getCommand("settings").setExecutor(new CommandManager(this));
    }

    public IItemGlower getItemGlower() {
        return versionManager.getItemGlower();
    }

    public IMount getMount() {
        return versionManager.getMount();
    }

}
