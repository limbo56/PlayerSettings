package me.limbo56.playersettings;

import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.SettingsWatchlist;
import me.limbo56.playersettings.command.CommandManager;
import me.limbo56.playersettings.command.SubCommandExecutor;
import me.limbo56.playersettings.command.subcommand.*;
import me.limbo56.playersettings.configuration.*;
import me.limbo56.playersettings.database.SQLiteDatabase;
import me.limbo56.playersettings.database.SettingsDatabase;
import me.limbo56.playersettings.database.SqlDatabase;
import me.limbo56.playersettings.listeners.InventoryListener;
import me.limbo56.playersettings.listeners.ListenerManager;
import me.limbo56.playersettings.listeners.PlayerListener;
import me.limbo56.playersettings.menu.SettingsMenuManager;
import me.limbo56.playersettings.settings.DefaultSettings;
import me.limbo56.playersettings.settings.SettingsManager;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.PluginLogHandler;
import me.limbo56.playersettings.util.PluginUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class PlayerSettings extends JavaPlugin {
  // Managers
  private ConfigurationManager configurationManager;
  private SettingsDatabase<?> settingsDatabase;
  private SettingsManager settingsManager;
  private SettingsMenuManager settingsMenuManager;
  private UserManager userManager;
  private CommandManager commandManager;
  private ListenerManager listenerManager;
  // Configuration
  private PluginConfiguration pluginConfiguration;
  private SettingsConfiguration settingsConfiguration;
  private ItemsConfiguration itemsConfiguration;
  private MessagesConfiguration messagesConfiguration;
  // State
  private boolean reloading = false;

  @Override
  public void onEnable() {
    PlayerSettingsProvider.setPlugin(this);

    Instant loadStart = Instant.now();
    getLogger().info("Loading configuration files...");
    try {
      configurationManager = new ConfigurationManager();
      pluginConfiguration = configurationManager.loadConfiguration(new PluginConfiguration());
      settingsConfiguration = configurationManager.loadConfiguration(new SettingsConfiguration());
      itemsConfiguration = configurationManager.loadConfiguration(new ItemsConfiguration());
      messagesConfiguration = configurationManager.loadConfiguration(new MessagesConfiguration());
    } catch (ExecutionException e) {
      getLogger().severe("An exception occurred while loading the default configuration files:");
      e.printStackTrace();
      setEnabled(false);
      return;
    }

    // Setup debug logger
    getLogger().addHandler(new PluginLogHandler());
    if (getPluginConfiguration().getBoolean("general.debug")) {
      getLogger().setLevel(Level.FINE);
    }

    getLogger().info("Connecting data manager...");
    initializeDataManager();
    settingsDatabase.connect();

    getLogger().info("Registering service managers...");
    userManager = new UserManager();
    settingsManager = new SettingsManager();
    Bukkit.getServicesManager()
        .register(SettingsWatchlist.class, userManager, this, ServicePriority.Normal);
    Bukkit.getServicesManager()
        .register(SettingsContainer.class, settingsManager, this, ServicePriority.Normal);

    getLogger().info("Loading internal managers...");
    commandManager = new CommandManager();
    listenerManager = new ListenerManager();
    settingsMenuManager = new SettingsMenuManager();
    registerDefaultSettings();
    registerDefaultListeners();
    registerSettingsCommand();
    userManager.loadOnlineUsers();

    // Log startup time and update message
    long startupTime = Duration.between(loadStart, Instant.now()).toMillis();
    getLogger().info("Successfully loaded (took " + startupTime + "ms)");
    PluginUpdater.logUpdateMessage();

    // Start bStats metrics
    if (PlayerSettingsProvider.hasMetricsEnabled()) {
      new Metrics(this, 16730);
    }

    // Register PlaceholderAPI expansion
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new PlayerSettingsPlaceholders(this).register();
    }
  }

  @Override
  public void onDisable() {
    getLogger().info("Save all users...");
    userManager.saveAll();

    getLogger().info("Disconnecting data manager...");
    settingsDatabase.disconnect();

    getLogger().info("Unloading internal managers...");
    userManager.unloadAll();
    settingsMenuManager.unloadAll();
    listenerManager.unloadAll();
    commandManager.unloadAll();
    settingsManager.unloadAll();
    configurationManager.unloadAll();
  }

  public void initializeDataManager() {
    ConfigurationSection storage = getPluginConfiguration().getConfigurationSection("storage");
    boolean hasStorageEnabled = storage != null && storage.getBoolean("enabled");
    settingsDatabase = hasStorageEnabled ? new SqlDatabase(storage) : new SQLiteDatabase(storage);
  }

  private void registerDefaultSettings() {
    DefaultSettings.getSettings().forEach(settingsManager::registerSetting);
    getSettingsConfiguration().getEnabledSettings().stream()
        .filter(setting -> !settingsManager.isSettingLoaded(setting.getName()))
        .forEach(settingsManager::loadSetting);
  }

  private void registerDefaultListeners() {
    listenerManager.registerListener(new PlayerListener());
    listenerManager.registerListener(new InventoryListener());
  }

  private void registerSettingsCommand() {
    commandManager.registerSubCommand(new HelpSubCommand());
    commandManager.registerSubCommand(new OpenSubCommand());
    commandManager.registerSubCommand(new ReloadSubCommand());
    commandManager.registerSubCommand(new SetSubCommand());
    commandManager.registerSubCommand(new GetSubCommand());

    SubCommandExecutor executor = new SubCommandExecutor();
    PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand("settings"));
    pluginCommand.setExecutor(executor);
    pluginCommand.setTabCompleter(executor);
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

  public UserManager getUserManager() {
    return userManager;
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }

  public SettingsManager getSettingsManager() {
    return settingsManager;
  }

  public ListenerManager getListenerManager() {
    return listenerManager;
  }

  public SettingsDatabase<?> getSettingsDatabase() {
    return settingsDatabase;
  }

  public SettingsMenuManager getSettingsMenuManager() {
    return settingsMenuManager;
  }

  public YamlConfiguration getPluginConfiguration() {
    return pluginConfiguration.getFile();
  }

  public SettingsConfiguration getSettingsConfiguration() {
    return settingsConfiguration;
  }

  public ItemsConfiguration getItemsConfiguration() {
    return itemsConfiguration;
  }

  public YamlConfiguration getMessagesConfiguration() {
    return messagesConfiguration.getFile();
  }
}
