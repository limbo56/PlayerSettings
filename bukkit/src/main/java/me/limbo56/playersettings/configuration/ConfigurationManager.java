package me.limbo56.playersettings.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.concurrent.ExecutionException;

public class ConfigurationManager {
  private final LoadingCache<String, YamlConfiguration> configurations =
      CacheBuilder.newBuilder().build(new ConfigurationLoader());

  public <T extends BaseConfiguration> T loadConfiguration(T configuration)
      throws ExecutionException {
    configurations.get(configuration.getFileName());
    return configuration;
  }

  /** Invalidates all the cached configurations and reloads them. */
  public void reloadConfigurations() {
    for (String configuration : configurations.asMap().keySet()) {
      configurations.refresh(configuration);
    }
  }

  public void unloadAll() {
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
}
