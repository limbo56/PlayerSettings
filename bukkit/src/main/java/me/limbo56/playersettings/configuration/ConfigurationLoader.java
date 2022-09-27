package me.limbo56.playersettings.configuration;

import com.google.common.cache.CacheLoader;
import java.io.File;
import java.io.InputStream;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class ConfigurationLoader extends CacheLoader<String, YamlConfiguration> {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @Override
  public @NotNull YamlConfiguration load(@NotNull String fileName) {
    // Create plugin data folder
    if (!plugin.getDataFolder().exists()) {
      plugin.getDataFolder().mkdirs();
    }

    // Initialize configuration file
    File configurationFile = ConfigUtil.getPluginFile(fileName);

    // Load template if configuration file doesn't exist
    if (!configurationFile.exists()) {
      InputStream templateResource = plugin.getResource(fileName);
      if (templateResource != null) {
        plugin.saveResource(fileName, false);
      }
    }

    // Load configuration file
    return YamlConfiguration.loadConfiguration(configurationFile);
  }
}
