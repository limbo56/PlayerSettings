package me.limbo56.playersettings.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.util.Configurations;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public abstract class BaseConfiguration {
  protected final PlayerSettings plugin;
  protected YamlConfiguration configuration;

  public BaseConfiguration(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  @NotNull
  abstract String getFileName();

  /**
   * Writes the contents of a {@link ConfigurationSerializable} to the specified {@code path}
   *
   * @param path Path to write object
   * @param serializable {@link ConfigurationSerializable} to write
   */
  protected void writeSerializable(String path, ConfigurationSerializable serializable) {
    serializable.serialize().forEach((key, value) -> configuration.set(path + "." + key, value));
  }

  public void save() throws IOException {
    getFile().save(Configurations.getPluginFile(getFileName()));
  }

  public void load() {
    if (!isLoaded()) {
      // Create plugin data folder
      if (!plugin.getDataFolder().exists()) {
        plugin.getDataFolder().mkdirs();
      }

      // Load template if configuration file doesn't exist
      String fileName = getFileName();
      File configurationFile = Configurations.getPluginFile(fileName);
      if (!configurationFile.exists()) {
        InputStream templateResource = plugin.getResource(fileName);
        if (templateResource != null) {
          plugin.saveResource(fileName, false);
        }
      }

      configuration = YamlConfiguration.loadConfiguration(configurationFile);
    }
  }

  public void reload() {
    configuration = null;
    load();
  }

  public boolean isLoaded() {
    return configuration != null;
  }

  public YamlConfiguration getFile() {
    return configuration;
  }
}
