package me.limbo56.playersettings.api.registry;

import java.util.Collection;
import me.limbo56.playersettings.api.Setting;
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
 * container.register(exampleSetting);
 * }</pre>
 */
public interface SettingsContainer {
  /**
   * Registers a new setting.
   *
   * @param setting The setting to register.
   */
  void register(Setting setting);

  /**
   * Unregisters a setting by its name.
   *
   * @param settingName The name of the setting to unregister.
   */
  void unregister(String settingName);

  /**
   * Retrieves a registered setting by its name.
   *
   * @param settingName The name of the setting to retrieve.
   * @param <T> The type of the setting.
   * @return The setting instance if found, null otherwise.
   */
  <T extends Setting> T getSetting(String settingName);

  /**
   * Retrieves all registered settings.
   *
   * @return A collection of all registered settings.
   */
  Collection<? extends Setting> getSettings();
}
