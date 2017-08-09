package me.limbo56.settings;

import com.statiocraft.jukebox.scJukeBox;
import me.limbo56.settings.config.DefaultConfiguration;
import me.limbo56.settings.config.MenuConfiguration;
import me.limbo56.settings.config.MessageConfiguration;
import me.limbo56.settings.listeners.AuthMeListener;
import me.limbo56.settings.listeners.FlyToggleListener;
import me.limbo56.settings.listeners.PlayerListener;
import me.limbo56.settings.listeners.WorldListener;
import me.limbo56.settings.managers.CommandManager;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.managers.MySqlManager;
import me.limbo56.settings.managers.PlayerManager;
import me.limbo56.settings.menu.SettingsMenu;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.PlayerUtils;
import me.limbo56.settings.utils.Updater;
import me.limbo56.settings.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:36 AM
 * <p>
 * Contributors: Maxetto
 */
public class PlayerSettings extends JavaPlugin {

    private static PlayerSettings instance;

    private static MySqlManager mySqlManager;

    private static PlayerManager playerManager;

    private static String serverVersion = "v1.8.8";

    public SortedMap<Integer, String> commandHelp = new TreeMap<>();

    private PluginManager pm = Bukkit.getServer().getPluginManager();

    public static PlayerSettings getInstance() {
        return instance;
    }

    public static MySqlManager getMySqlManager() {
        return mySqlManager;
    }

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    /**
     * Method to log a string on console.
     *
     * @param message Message.
     */
    public void log(String message) {
        System.out.println("PlayerSettings >> " + message);
    }

    /**
     * Method called when plugin is enabled.
     */
    public void onEnable() {
        // Initialize instance
        instance = this;

        // Default server version
        serverVersion = "v1.8.8";

        try {
            // Get server version
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            log("This server version is not supported!");
            this.setEnabled(false);
        }

        log("======================================================");
        log("PlayerSettings v" + getDescription().getVersion() + " is being loaded...");

        log("");

        log("Loading all data...");

        // Load plugin defaults
        loadDefaults();

        // Connect to database if it's enabled
        if (getConfig().getBoolean("MySQL.enable")) {
            log("");
            log("Connecting to database...");

            mySqlManager.openConnection();
            mySqlManager.createTable();

            log("");
        }

        // Check for settings incompatibility
        if (ConfigurationManager.getDefault().getBoolean("Default.Fly") && ConfigurationManager.getDefault().getBoolean("Default.DoubleJump")) {
            log("ALERT: You cannot have both Fly and DoubleJump enabled by default! Disabling DoubleJump");
            ConfigurationManager.getDefault().set("Default.DoubleJump", false);
            ConfigurationManager.getDefault().saveConfig();
            ConfigurationManager.getDefault().reloadConfig();
        }

        log("All data has been loaded");
        log("");

        log("PlayerSettings successfully finished loading!");
        log("Your server is running version " + serverVersion);
        log("======================================================");
        log("");

        log("Checking for updates...");
        log("");

        // Send updater
        Updater.sendUpdater();

        log("");

        // Load online players
        PlayerUtils.loadOnlinePlayers();
    }

    /**
     * Method called when plugin is disabled.
     */
    public void onDisable() {

        // Save all cached player's settings
        if (!playerManager.getPlayerList().isEmpty()) {
            for (Entry<UUID, CustomPlayer> entry : playerManager.getPlayerList().entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());

                if (Utilities.hasAuthMePlugin() && !Utilities.isAuthenticated(player))
                    return;

                CustomPlayer cPlayer = entry.getValue();

                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                    if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
                        cPlayer.setFly(ConfigurationManager.getDefault().getBoolean("Default.Fly"));

                    if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled"))
                        cPlayer.setDoubleJump(ConfigurationManager.getDefault().getBoolean("Default.DoubleJump"));
                }

                playerManager.saveSettingsSync(playerManager.getPlayerList().get(player.getUniqueId()));

                if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.removePotionEffect(PotionEffectType.JUMP);
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);

                    if (Utilities.hasRadioPlugin()) {
                        if (scJukeBox.getCurrentJukebox(player) != null)
                            scJukeBox.getCurrentJukebox(player).removePlayer(player);
                    }

                }

            }

            playerManager.getPlayerList().clear();
        }

        // Uninitialize variables
        instance = null;
        pm = null;

        // Disable plugin
        Bukkit.getPluginManager().disablePlugin(this);
    }

    private void setupConfig() {
        // Get config.yml
        File file = new File(getDataFolder(), "config.yml");

        // Boolean if config was created
        boolean created;

        // If config doesn't exists, then create one
        if (!file.exists()) {
            created = file.getParentFile().mkdir();
            log("Config file doesn't exist yet.");
            log("Creating Config File and loading it.");

            if (created) {
                log("File config.yml created with success!");
                log("");
            }

        }

        // Get plugin configuration
        ConfigurationManager config = ConfigurationManager.getConfig();

        // Add default lines
        config.addDefault("MySQL.enable", false);
        config.addDefault("MySQL.host", "localhost");
        config.addDefault("MySQL.port", 3306);
        config.addDefault("MySQL.database", "playersettings");
        config.addDefault("MySQL.name", "root");
        config.addDefault("MySQL.password", "");

        config.addDefault("Update-Message", true);

        // Remove Using-Citizens line if it exists
        // Citizens is not needed anymore, you can just check for npc by his metadata.
        if (config.contains("Using-Citizens")) {
            config.set("Using-Citizens", null);
        }

        // Add default lines
        config.addDefault("Debug", false);
        config.saveConfig();
    }

    private void loadDefaults() {
        // Setup config.yml
        setupConfig();

        // Create MySqlManager instance
        mySqlManager = new MySqlManager();

        // Create PlayerManager instance
        playerManager = new PlayerManager();

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
        if (Utilities.hasAuthMePlugin())
            pm.registerEvents(new AuthMeListener(), this);

        // Creating help messages
        commandHelp.put(1, "§a/settings open §7- §cOpen the settings menu.");
        commandHelp.put(2, "§a/settings reload §7- §cReloads all config files.");

        // Register commands.
        getCommand("settings").setExecutor(new CommandManager(this));
    }
}
