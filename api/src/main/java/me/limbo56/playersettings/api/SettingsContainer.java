package me.limbo56.playersettings.api;

import me.limbo56.playersettings.api.setting.Setting;
import org.bukkit.plugin.ServicesManager;

/**
 * A registry of {@link Setting settings}.
 *
 * <p>You can get an instance of this class using the Bukkit service manager API {@link
 * ServicesManager#load(Class)}.
 *
 * <p>Below is an example of how to load the {@link SettingsContainer} and use it to register a
 * setting.
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
   * Adds a {@link Setting} to the settings container
   *
   * @param setting Setting to register
   */
  void registerSetting(Setting setting);

  /**
   * Removes a {@link Setting} from the settings container
   *
   * @param settingName Setting to unregister
   */
  void unregisterSetting(String settingName);

  /**
   * Gets a registered {@link Setting} by its name
   *
   * @param settingName The name of the setting
   */
  Setting getSetting(String settingName);
}
