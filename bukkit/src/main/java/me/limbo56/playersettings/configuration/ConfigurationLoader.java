package me.limbo56.playersettings.configuration;

import com.google.common.cache.CacheLoader;
import java.lang.reflect.InvocationTargetException;
import me.limbo56.playersettings.PlayerSettings;
import org.jetbrains.annotations.NotNull;

public class ConfigurationLoader
    extends CacheLoader<Class<? extends BaseConfiguration>, BaseConfiguration> {
  private final PlayerSettings plugin;

  public ConfigurationLoader(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  @Override
  public @NotNull BaseConfiguration load(@NotNull Class<? extends BaseConfiguration> clazz) {
    try {
      return clazz.getConstructor(PlayerSettings.class).newInstance(plugin);
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
