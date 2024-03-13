package me.limbo56.playersettings.hook;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.hook.placeholder.PlaceholdersHook;
import me.limbo56.playersettings.util.PluginLogger;

public class HookManager {
  private final PlayerSettings plugin;

  public HookManager(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  public void registerDefaultHooks() {
    PluginLogger.info("Registering hooks...");
    registerHook(new ServicesHook(plugin));
    registerHook(new PlaceholdersHook(plugin));
    registerHook(new AdventureHook(plugin));
    registerHook(new MetricsHook(plugin));
  }

  public void registerHook(Hook hook) {
    if (hook.shouldRegister()) {
      hook.register();
    }
  }
}
