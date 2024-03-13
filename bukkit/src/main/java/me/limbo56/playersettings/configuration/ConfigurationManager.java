package me.limbo56.playersettings.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.util.PluginLogger;

public class ConfigurationManager {
  private final LoadingCache<Class<? extends BaseConfiguration>, BaseConfiguration> configurations;

  public ConfigurationManager(PlayerSettings plugin) {
    this.configurations = CacheBuilder.newBuilder().build(new ConfigurationLoader(plugin));
  }

  public void loadConfigurations() {
    PluginLogger.info("Loading configuration files...");
    getConfiguration(PluginConfiguration.class).load();
    getConfiguration(SettingsConfiguration.class).load();
    getConfiguration(ItemsConfiguration.class).load();
    getConfiguration(MessagesConfiguration.class).load();
  }

  /** Invalidates all the cached configurations and reloads them. */
  public void reloadConfigurations() {
    for (BaseConfiguration configuration : configurations.asMap().values()) {
      configuration.reload();
    }
  }

  public void unloadAll() {
    configurations.invalidateAll();
  }

  /**
   * Gets a {@link BaseConfiguration} instance that is cached by its class.
   *
   * <p>If the configuration class has not been cached, an instance will be created and load the
   * contents existing configuration.
   *
   * <p>If the configuration does not exist, it will be created and the default template will be
   * written to it.
   *
   * @param clazz Class of the configuration.
   * @return The loaded configuration.
   */
  public <T extends BaseConfiguration> T getConfiguration(Class<T> clazz) {
    try {
      return (T) configurations.get(clazz);
    } catch (ExecutionException exception) {
      PluginLogger.severe(
          "An exception occurred while loading configuration '" + clazz.getSimpleName() + "'",
          exception);
      return null;
    }
  }
}
