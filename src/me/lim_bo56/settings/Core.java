package me.lim_bo56.settings;

import me.lim_bo56.settings.cmds.CommandManager;
import me.lim_bo56.settings.listeners.FlyToggleListener;
import me.lim_bo56.settings.listeners.PlayerListener;
import me.lim_bo56.settings.listeners.WorldListener;
import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.managers.DefaultManager;
import me.lim_bo56.settings.managers.MenuManager;
import me.lim_bo56.settings.managers.MessageManager;
import me.lim_bo56.settings.menus.SettingsMenu;
import me.lim_bo56.settings.mysql.MySqlConnection;
import me.lim_bo56.settings.objects.CustomPlayer;
import me.lim_bo56.settings.utils.ColorUtils;
import me.lim_bo56.settings.utils.Updater;
import me.lim_bo56.settings.utils.Variables;
import me.lim_bo56.settings.versionmanager.IItemGlower;
import me.lim_bo56.settings.versionmanager.IMount;
import me.lim_bo56.settings.versionmanager.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

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
    private static void log(String object) {
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

        log("");
        log("Loading all data...");

        loadDefaults(version);

        log("All data has been loaded");
        log("");

        if (getConfig().getBoolean("MySQL.enable")) {
            log("Connecting to database...");

            MySqlConnection.getInstance().openConnection();
            MySqlConnection.getInstance().checkTable();

            log("Connected to database");
            log("");
        }

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

        checkForOnlinePlayers();

    }

    private void loadDefaults(String version) {

        // Save default config
        saveDefaultConfig();

        // Get server version.
        versionManager = new VersionManager(version);
        versionManager.load();

        // Load Menu Defaults

        new MenuManager();

        // Load messages.
        new MessageManager();

        // Load data
        new DefaultManager();

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

    private void checkForOnlinePlayers() {

        ConfigurationManager menu = ConfigurationManager.getMenu();

        if (Bukkit.getOnlinePlayers() != null)
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {

                    CustomPlayer cPlayer = new CustomPlayer(player);

                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.removePotionEffect(PotionEffectType.JUMP);
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);

                    if (!ConfigurationManager.getConfig().getBoolean("MySQL.enable")) {

                        if (Variables.defaultVisibility) cPlayer.setVisibility(true);
                        if (Variables.defaultStacker) cPlayer.setStacker(true);
                        if (Variables.defaultChat) cPlayer.setChat(true);
                        if (Variables.defaultVanish) cPlayer.setVanish(true);
                        if (Variables.defaultFly) cPlayer.setFly(true);
                        if (Variables.defaultSpeed) cPlayer.setSpeed(true);
                        if (Variables.defaultJump) cPlayer.setJump(true);

                    }

                    if (cPlayer.hasVisibility()) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(online);
                        }
                    }

                    if (cPlayer.hasVanish()) {

                        player.addPotionEffect(Variables.INVISIBILITY);

                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.hidePlayer(player);
                        }

                    }

                    if (cPlayer.hasFly()) {
                        player.setAllowFlight(true);
                    }

                    if (cPlayer.hasSpeed()) {
                        player.addPotionEffect(Variables.SPEED);
                    }

                    if (cPlayer.hasJump()) {
                        player.addPotionEffect(Variables.JUMP);
                    }

                    if (player.isOp()) {
                        player.sendMessage(ColorUtils.Color(Variables.CHAT_TITLE + Updater.playerUpdater()));
                    }
                } else if (!menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        CustomPlayer oPlayer = new CustomPlayer(online);

                        if (oPlayer.hasVanish()) {
                            online.hidePlayer(player);
                        } else {
                            player.showPlayer(online);
                        }

                    }
                }

            }

    }

    public IItemGlower getItemGlower() {
        return versionManager.getItemGlower();
    }

    public IMount getMount() {
        return versionManager.getMount();
    }

}
