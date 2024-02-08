package me.limbo56.playersettings.hook;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.registry.SettingsContainer;
import me.limbo56.playersettings.api.registry.SettingsWatchlist;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class ServicesHook extends Hook {
  public ServicesHook(PlayerSettings plugin) {
    super(plugin);
  }

  @Override
  public void register() {
    PluginLogger.info("Registering service managers...");
    ServicesManager servicesManager = Bukkit.getServicesManager();
    servicesManager.register(
        SettingsWatchlist.class, plugin.getUserManager(), plugin, ServicePriority.Normal);
    servicesManager.register(
        SettingsContainer.class, plugin.getSettingsManager(), plugin, ServicePriority.Normal);
  }
}
