package me.limbo56.playersettings.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.util.ConfigUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationManager {
  private final LoadingCache<String, YamlConfiguration> configurations =
      CacheBuilder.newBuilder().build(new ConfigurationLoader());

  /** Invalidates all the cached configurations and reloads them. */
  public void reloadConfigurations() {
    for (Entry<String, YamlConfiguration> entry : getConfigurations().entrySet()) {
      String fileName = entry.getKey();
      YamlConfiguration configuration = entry.getValue();
      try {
        File configurationFile = ConfigUtil.getPluginFile(fileName);
        configuration.load(configurationFile);
      } catch (IOException | InvalidConfigurationException e) {
        PlayerSettingsProvider.getPlugin()
            .getLogger()
            .severe("Failed to reload configuration '" + fileName + "'");
        e.printStackTrace();
      }
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
