package me.limbo56.playersettings;

import me.limbo56.playersettings.command.CommandManager;
import me.limbo56.playersettings.configuration.*;
import me.limbo56.playersettings.database.DataManager;
import me.limbo56.playersettings.hook.AdventureHook;
import me.limbo56.playersettings.hook.HookManager;
import me.limbo56.playersettings.lib.LibraryManager;
import me.limbo56.playersettings.listener.ListenerManager;
import me.limbo56.playersettings.menu.SettingsMenuManager;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.Timer;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerSettings extends JavaPlugin {
  private static PlayerSettings INSTANCE;
  // Managers
  private final LibraryManager libraryManager = new LibraryManager(this);
  private final HookManager hookManager = new HookManager(this);
  private final ConfigurationManager configurationManager = new ConfigurationManager(this);
  private final DataManager dataManager = new DataManager(this);
  private final UserManager userManager = new UserManager(this);
  private final SettingsManager settingsManager = new SettingsManager(this);
  private final SettingsMenuManager settingsMenuManager = new SettingsMenuManager(this);
  private final CommandManager commandManager = new CommandManager(this);
  private final ListenerManager listenerManager = new ListenerManager(this);
  private Messenger messenger;
  // State
  private boolean reloading = false;

  public static PlayerSettings getInstance() {
    return INSTANCE;
  }

  @Override
  public void onEnable() {
    Timer timer = Timer.start();

    // Load essential components
    INSTANCE = this;
    libraryManager.loadLibraries();
    configurationManager.loadConfigurations();
    dataManager.connect();

    // Register defaults
    settingsManager.registerDefaultSettings();
    commandManager.registerDefaultCommands();
    listenerManager.registerDefaultListeners();
    hookManager.registerDefaultHooks();

    // Load settings
    userManager.loadOnlineUsers();

    // Log startup time and update message
    PluginLogger.logVersionMessage();
    timer.stop();
    PluginLogger.logElapsedMillis("Successfully enabled", timer);
  }

  @Override
  public void onDisable() {
    userManager.saveUsers();
    dataManager.disconnect();

    PluginLogger.info("Unloading internal managers...");
    userManager.unloadAll();
    settingsMenuManager.unloadAll();
    listenerManager.unloadAll();
    commandManager.unloadAll();
    settingsManager.unloadAll();
    configurationManager.unloadAll();
    AdventureHook.unloadAdventure();
  }

  public void setReloading(boolean reloading) {
    this.reloading = reloading;
  }

  public boolean isReloading() {
    return reloading;
  }

  public PluginConfiguration getConfiguration() {
    return configurationManager.getConfiguration(PluginConfiguration.class);
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

  public SettingsMenuManager getSettingsMenuManager() {
    return settingsMenuManager;
  }

  public DataManager getDataManager() {
    return dataManager;
  }

  public Messenger getMessenger() {
    if (messenger == null) {
      this.messenger = new Messenger(this);
    }
    return messenger;
  }
}
