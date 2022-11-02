package me.limbo56.playersettings.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationManager {
  private final LoadingCache<String, YamlConfiguration> configurations =
    CacheBuilder.newBuilder().build(new ConfigurationLoader());

  /**
   * Invalidates all the cached configurations and reloads them.
   */
  public void reloadConfigurations() {
    for (String configuration : getConfigurations().keySet()) {
      configurations.refresh(configuration);
    }
  }

  public void invalidateAll() {
    configurations.invalidateAll();
  }

  /**
   * Loads the configuration file with the given name from the plugin's data folder.
   *
   * <p>If the configuration file does not exist, it will be created and the default template will
   * be written to it.
   *
   * @param fileName Name of the configuration.
   * @return The loaded configuration.
   */
  public YamlConfiguration getConfiguration(String fileName) throws ExecutionException {
    return configurations.get(fileName);
  }

  public <T extends PluginConfiguration> T getConfiguration(T configuration) {
    configurations.refresh(configuration.getFileName());
    return configuration;
  }

  /**
   * Gets an immutable copy of the map of configurations.
   *
   * @return Map of configurations
   */
  public Map<String, YamlConfiguration> getConfigurations() {
    return configurations.asMap();
  }
}
