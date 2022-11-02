package me.limbo56.playersettings;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.SettingsWatchlist;
import me.limbo56.playersettings.command.CommandManager;
import me.limbo56.playersettings.command.SubCommandExecutor;
import me.limbo56.playersettings.command.subcommand.GetSubCommand;
import me.limbo56.playersettings.command.subcommand.HelpSubCommand;
import me.limbo56.playersettings.command.subcommand.OpenSubCommand;
import me.limbo56.playersettings.command.subcommand.ReloadSubCommand;
import me.limbo56.playersettings.command.subcommand.SetSubCommand;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.database.DataManager;
import me.limbo56.playersettings.database.SqlDataManager;
import me.limbo56.playersettings.database.SqlLiteDataManager;
import me.limbo56.playersettings.listeners.InventoryListener;
import me.limbo56.playersettings.listeners.ListenerManager;
import me.limbo56.playersettings.listeners.PlayerListener;
import me.limbo56.playersettings.settings.DefaultSetting;
import me.limbo56.playersettings.settings.DefaultSettingsContainer;
import me.limbo56.playersettings.user.SettingUserManager;
import me.limbo56.playersettings.util.PluginLogHandler;
import me.limbo56.playersettings.util.PluginUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerSettings extends JavaPlugin {
  // Managers
  private ConfigurationManager configurationManager;
  private SettingUserManager userManager;
  private CommandManager commandManager;
  private DefaultSettingsContainer settingsContainer;
  private ListenerManager listenerManager;
  private DataManager dataManager;
  // Configuration
  private YamlConfiguration pluginConfiguration;
  private SettingsConfiguration settingsConfiguration;
  private YamlConfiguration itemsConfiguration;
  private YamlConfiguration messagesConfiguration;
  // State
  private boolean reloading = false;

  @Override
  public void onEnable() {
    Instant loadStart = Instant.now();

    PlayerSettingsProvider.setPlugin(this);
    getLogger().info("Loading internal managers...");
    userManager = new SettingUserManager();
    configurationManager = new ConfigurationManager();
    commandManager = new CommandManager();
    settingsContainer = new DefaultSettingsContainer();
    listenerManager = new ListenerManager();

    getLogger().info("Loading configuration files...");
    try {
      pluginConfiguration = configurationManager.getConfiguration("config.yml");
      settingsConfiguration = configurationManager.getConfiguration(new SettingsConfiguration());
      itemsConfiguration = configurationManager.getConfiguration("items.yml");
      messagesConfiguration = configurationManager.getConfiguration("messages.yml");
    } catch (ExecutionException e) {
      getLogger().severe("Failed to load configuration files!");
      e.printStackTrace();
      setEnabled(false);
      return;
    }

    // Setup debug logger
    getLogger().addHandler(new PluginLogHandler());
    if (pluginConfiguration.getBoolean("general.debug")) {
      getLogger().setLevel(Level.FINE);
    }

    getLogger().info("Connecting data manager...");
    initializeDataManager();
    dataManager.connect();
    dataManager.createDefaultTable();

    getLogger().info("Registering service managers...");
    Bukkit.getServicesManager()
        .register(SettingsWatchlist.class, userManager, this, ServicePriority.Normal);
    Bukkit.getServicesManager()
        .register(SettingsContainer.class, settingsContainer, this, ServicePriority.Normal);

    getLogger().info("Loading default settings...");
    DefaultSetting.getSettings().forEach(settingsContainer::registerSetting);
    settingsConfiguration.getSettingsFromConfiguration().forEach(settingsContainer::loadSetting);

    // Load commands, listeners and online players
    SubCommandExecutor executor = new SubCommandExecutor();
    PluginCommand pluginCommand = Bukkit.getPluginCommand("settings");
    commandManager.registerSubCommand(new HelpSubCommand());
    commandManager.registerSubCommand(new OpenSubCommand());
    commandManager.registerSubCommand(new ReloadSubCommand());
    commandManager.registerSubCommand(new SetSubCommand());
    commandManager.registerSubCommand(new GetSubCommand());
    listenerManager.registerListener(new PlayerListener());
    listenerManager.registerListener(new InventoryListener());
    userManager.loadOnlineUsers();
    pluginCommand.setExecutor(executor);
    pluginCommand.setTabCompleter(executor);

    // Log startup time and update message
    Instant loadFinish = Instant.now();
    long startupTime = Duration.between(loadStart, loadFinish).toMillis();
    getLogger().info("Successfully loaded (took " + startupTime + "ms)");
    PluginUpdater.logUpdateMessage();

    // Start bStats metrics
    if (PlayerSettingsProvider.hasMetricsEnabled()) {
      new Metrics(this, 16730);
    }
  }

  @Override
  public void onDisable() {
    getLogger().info("Unloading users...");
    userManager.unloadAllUsers();

    getLogger().info("Disconnecting data manager...");
    dataManager.disconnect();

    getLogger().info("Unloading internal managers...");
    userManager.invalidateAll();
    listenerManager.unregisterAll();
    commandManager.unregisterAll();
    settingsContainer.unloadAll();
    configurationManager.invalidateAll();
  }

  public void initializeDataManager() {
    ConfigurationSection database = pluginConfiguration.getConfigurationSection("storage");
    this.dataManager =
        database.getBoolean("enabled") ? new SqlDataManager(database) : new SqlLiteDataManager();
  }

  public boolean isReloading() {
    return reloading;
  }

  public void setReloading(boolean reloading) {
    this.reloading = reloading;
  }

  public ConfigurationManager getConfigurationManager() {
    return configurationManager;
  }

  public SettingUserManager getUserManager() {
    return userManager;
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }

  public DefaultSettingsContainer getSettingsContainer() {
    return settingsContainer;
  }

  public ListenerManager getListenerManager() {
    return listenerManager;
  }

  public DataManager getDataManager() {
    return dataManager;
  }

  public YamlConfiguration getPluginConfiguration() {
    return pluginConfiguration;
  }

  public SettingsConfiguration getSettingsConfiguration() {
    return settingsConfiguration;
  }

  public YamlConfiguration getItemsConfiguration() {
    return itemsConfiguration;
  }

  public YamlConfiguration getMessagesConfiguration() {
    return messagesConfiguration;
  }
}
