package me.lim_bo56.lnpp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.lim_bo56.lnpp.cmds.Preferences;
import me.lim_bo56.lnpp.cmds.PreferencesLobby;
import me.lim_bo56.lnpp.cmds.PreferencesPlayer;
import me.lim_bo56.lnpp.menus.listeners.LobbyMenuListener;
import me.lim_bo56.lnpp.menus.listeners.MainMenuListener;
import me.lim_bo56.lnpp.menus.listeners.PlayerMenuListener;
import me.lim_bo56.lnpp.menus.listeners.WorldListener;
import me.lim_bo56.lnpp.utils.ItemFactory;
import me.lim_bo56.lnpp.utils.UpdateChecker;

/**
 * 
 * @author lim_bo56
 *
 */

public class MainPreferences extends JavaPlugin {

	private static MainPreferences instance;

	// All the arraylist to store players.
	public ArrayList<Player> Visibility = new ArrayList<Player>();
	public ArrayList<Player> Speed = new ArrayList<Player>();
	public ArrayList<Player> Jump = new ArrayList<Player>();
	public ArrayList<Player> Fly = new ArrayList<Player>();
	public ArrayList<Player> Chat = new ArrayList<Player>();
	public ArrayList<Player> Stacker = new ArrayList<Player>();
	public ArrayList<Player> Vanish = new ArrayList<Player>();

	public static MainPreferences getInstance() {
		return instance;
	}

	/**
	 * Method to register all the listeners of the plugin.
	 */
	public void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new PlayerMenuListener(this), this);
		pm.registerEvents(new MainMenuListener(this), this);
		pm.registerEvents(new LobbyMenuListener(this), this);
		pm.registerEvents(new WorldListener(this), this);
	}

	public void onEnable() {
		instance = this;

		// Register listeners
		registerListeners();

		// Register commands
		getCommand("preferences").setExecutor(new Preferences());
		getCommand("preferencesLobby").setExecutor(new PreferencesLobby());
		getCommand("preferencesPlayer").setExecutor(new PreferencesPlayer());

		// Message sent in the console when the plugin starts.
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "*********************************");
		getServer().getConsoleSender().sendMessage(
				ChatColor.AQUA + "*" + ChatColor.GREEN + "Lobby&PlayerPreferences enabled" + ChatColor.AQUA + "*");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "*     " + "                          *");
		getServer().getConsoleSender().sendMessage(
				ChatColor.AQUA + "*" + ChatColor.RED + "         by: lim_bo56          " + ChatColor.AQUA + "*");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "*********************************");

		getServer().getConsoleSender().sendMessage(UpdateChecker.getInstance().ConsoleUpdateChecker());
		getServer().getConsoleSender().sendMessage("§aDownload§f>§7> §fhttps://goo.gl/853ZVt");

		saveDefaultConfig();

		// Enable the plugin if the server version is compatible.
		if (ItemFactory.getInstance().setupGlow()) {

			getLogger().info("Setup was successful!");
			getLogger().info("The plugin setup process is complete!");

			// Disable the plugin if the server version isn't compatible.
		} else {

			getLogger().severe("Failed to setup!");
			getLogger().severe("Your server version is not compatible with this plugin!");

			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	public void onDisable() {

		// Message sent to the console when the plugin is disabled.
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "*********************************");
		getServer().getConsoleSender().sendMessage(
				ChatColor.AQUA + "*" + ChatColor.GREEN + "Lobby&PlayerPreferences disabled" + ChatColor.AQUA + "*");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "*     " + "                          *");
		getServer().getConsoleSender().sendMessage(
				ChatColor.AQUA + "*" + ChatColor.RED + "         by: lim_bo56          " + ChatColor.AQUA + "*");
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "*********************************");
	}

}
