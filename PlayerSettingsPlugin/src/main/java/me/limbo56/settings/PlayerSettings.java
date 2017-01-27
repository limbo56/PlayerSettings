package me.limbo56.settings;

import me.limbo56.settings.config.DefaultConfiguration;
import me.limbo56.settings.config.MenuConfiguration;
import me.limbo56.settings.config.MessageConfiguration;
import me.limbo56.settings.listeners.FlyToggleListener;
import me.limbo56.settings.listeners.PlayerListener;
import me.limbo56.settings.listeners.WorldListener;
import me.limbo56.settings.managers.CommandManager;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.managers.VersionManager;
import me.limbo56.settings.menus.SettingsMenu;
import me.limbo56.settings.mysql.MySqlConnection;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.Updater;
import me.limbo56.settings.utils.Utilities;
import me.limbo56.settings.version.IItemGlower;
import me.limbo56.settings.version.IMount;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.statiocraft.jukebox.scJukeBox;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:36 AM
 */
public class PlayerSettings extends JavaPlugin {

    private static PlayerSettings instance;

    public SortedMap<Integer, String> commandHelp = new TreeMap<>();

    private VersionManager versionManager;

    private PluginManager pm = Bukkit.getServer().getPluginManager();

    public static PlayerSettings getInstance() {
        return instance;
    }

    /**
     * Method to log a string on console.
     *
     * @param object Message.
     */
    public void log(String object) {
        System.out.println("Preferences >> " + object);
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
            this.setEnabled(false);
        }

        log("======================================================");
        log("PlayerSettings v" + getDescription().getVersion() + " is being loaded...");

        log("");

        if (getConfig().getBoolean("MySQL.enable")) {
            log("Connecting to database...");

            MySqlConnection.getInstance().openConnection();
            MySqlConnection.getInstance().createTable();

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
    
    public void onDisable() {
    	if (!Cache.PLAYER_LIST.isEmpty()) {
    		for (Player player : Cache.PLAYER_LIST.keySet()) {
    			Cache.PLAYER_LIST.get(player).saveSettings();
    			if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
    	            player.removePotionEffect(PotionEffectType.SPEED);
    	            player.removePotionEffect(PotionEffectType.JUMP);
    	            player.removePotionEffect(PotionEffectType.INVISIBILITY);
    	            if (Utilities.hasRadioPlugin()) {
    	                if (scJukeBox.getCurrentJukebox(player) != null)
    	                    scJukeBox.getCurrentJukebox(player).removePlayer(player);
    	            }
    	        }

    	        Cache.PLAYER_LIST.remove(player);
    		}
    	}
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
        if (config.contains("Using-Citizens")) {
            config.set("Using-Citizens", null);
        }
        config.addDefault("Debug", false);
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
