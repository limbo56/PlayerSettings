package me.limbo56.playersettings.configuration;

import com.google.common.cache.CacheLoader;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.util.Configurations;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public class ConfigurationLoader extends CacheLoader<String, YamlConfiguration> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public @NotNull YamlConfiguration load(@NotNull String fileName) {
    // Create plugin data folder
    if (!PLUGIN.getDataFolder().exists()) {
      PLUGIN.getDataFolder().mkdirs();
    }

    // Load template if configuration file doesn't exist
    File configurationFile = Configurations.getPluginFile(fileName);
    if (!configurationFile.exists()) {
      InputStream templateResource = PLUGIN.getResource(fileName);
      if (templateResource != null) {
        PLUGIN.saveResource(fileName, false);
      }
    }

    // Load configuration file
    return YamlConfiguration.loadConfiguration(configurationFile);
  }
}
