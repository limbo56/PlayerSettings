package me.limbo56.playersettings.api;

import me.limbo56.playersettings.api.setting.Setting;
import org.bukkit.plugin.ServicesManager;

/**
 * An object that stores and provides access to a registry of {@link Setting Settings}.
 *
 * <p>You can get an instance of this class by using the Bukkit {@link ServicesManager#load(Class)}
 * method. Below is an example that shows how to load and register a setting.
 *
 * <pre>{@code
 * SettingsContainer container = Bukkit.getServicesManager().load(SettingsContainer.class);
 *
 * // Register example setting
 * container.registerSetting(exampleSetting);
 * }</pre>
 */
public interface SettingsContainer {
  /**
   * Registers a {@link Setting} with the plugin
   *
   * @param setting Setting to register
   */
  void registerSetting(Setting setting);

  /**
   * Gets a registered {@link Setting} by its name
   *
   * @param settingName The name of the setting
   */
  Setting getSetting(String settingName);
}
