package me.limbo56.playersettings;

import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.SettingsWatchlist;
import me.limbo56.playersettings.command.CommandManager;
import me.limbo56.playersettings.command.SubCommandExecutor;
import me.limbo56.playersettings.command.subcommand.*;
import me.limbo56.playersettings.configuration.*;
import me.limbo56.playersettings.database.SettingsDatabase;
import me.limbo56.playersettings.database.SettingsDatabaseProvider;
import me.limbo56.playersettings.lib.Libraries;
import me.limbo56.playersettings.listeners.InventoryListener;
import me.limbo56.playersettings.listeners.ListenerManager;
import me.limbo56.playersettings.listeners.PlayerListener;
import me.limbo56.playersettings.menu.SettingsMenuManager;
import me.limbo56.playersettings.settings.DefaultSettings;
import me.limbo56.playersettings.settings.SettingsManager;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.PluginLogHandler;
import me.limbo56.playersettings.util.PluginUpdater;
import net.byteflux.libby.BukkitLibraryManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
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
    // Track load startup time
    Instant loadStartInstant = Instant.now();
    PlayerSettingsProvider.setPlugin(this);
    getLogger().addHandler(new PluginLogHandler());

    getLogger().info("Loading libraries...");
    BukkitLibraryManager bukkitLibraryManager = new BukkitLibraryManager(this);
    bukkitLibraryManager.addMavenCentral();
    Arrays.stream(Libraries.values())
        .forEach(libraries -> bukkitLibraryManager.loadLibrary(libraries.getLibrary()));

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
    if (pluginConfiguration.hasDebugEnabled()) {
      getLogger().setLevel(Level.FINE);
    }

    getLogger().info("Connecting data manager...");
    registerSettingsDatabase();

    getLogger().info("Loading internal managers...");
    commandManager = new CommandManager();
    listenerManager = new ListenerManager();
    settingsMenuManager = new SettingsMenuManager();
    listenerManager.registerListener(new PlayerListener());
    listenerManager.registerListener(new InventoryListener());

    PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand("settings"));
    SubCommandExecutor executor = new SubCommandExecutor();
    pluginCommand.setExecutor(executor);
    pluginCommand.setTabCompleter(executor);
    commandManager.registerSubCommand(new HelpSubCommand());
    commandManager.registerSubCommand(new OpenSubCommand());
    commandManager.registerSubCommand(new ReloadSubCommand());
    commandManager.registerSubCommand(new SetSubCommand());
    commandManager.registerSubCommand(new GetSubCommand());

    getLogger().info("Registering service managers...");
    userManager = new UserManager();
    settingsManager = new SettingsManager();
    Bukkit.getServicesManager()
        .register(SettingsWatchlist.class, userManager, this, ServicePriority.Normal);
    Bukkit.getServicesManager()
        .register(SettingsContainer.class, settingsManager, this, ServicePriority.Normal);

    // Register default settings
    DefaultSettings.getSettings().forEach(settingsManager::registerSetting);
    getSettingsConfiguration().getEnabledSettings(false).stream()
        .filter(setting -> !settingsManager.isSettingRegistered(setting.getName()))
        .forEach(setting -> settingsManager.registerSetting(setting, false));
    userManager.loadOnlineUsers();

    // Log startup time and update message
    long startupDuration = Duration.between(loadStartInstant, Instant.now()).toMillis();
    getLogger().info("Successfully loaded (took " + startupDuration + "ms)");
    PluginUpdater.logUpdateMessage();

    // Start bStats metrics
    if (pluginConfiguration.hasMetricsEnabled()) {
      new Metrics(this, 16730);
    }

    // Register placeholders
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

  public void registerSettingsDatabase() {
    ConfigurationSection storageSection =
        pluginConfiguration.getFile().getConfigurationSection("storage");
    if (storageSection == null) {
      setEnabled(false);
      throw new NullPointerException(
          "Empty or missing properties in the 'storage' section inside 'config.yml'");
    }
    settingsDatabase = SettingsDatabaseProvider.getSettingsDatabase(storageSection);
    settingsDatabase.connect();
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

  public PluginConfiguration getPluginConfiguration() {
    return pluginConfiguration;
  }

  public SettingsConfiguration getSettingsConfiguration() {
    return settingsConfiguration;
  }

  public ItemsConfiguration getItemsConfiguration() {
    return itemsConfiguration;
  }

  public MessagesConfiguration getMessagesConfiguration() {
    return messagesConfiguration;
  }
}
