package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.util.Configurations;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public abstract class BaseConfiguration {
  @NotNull
  abstract String getFileName();

  /**
   * Writes the contents of a {@link ConfigurationSerializable} to the specified {@code path}
   *
   * @param path Path to write object
   * @param serializable {@link ConfigurationSerializable} to write
   */
  public void writeSerializable(String path, ConfigurationSerializable serializable) {
    serializable.serialize().forEach((key, value) -> getFile().set(path + "." + key, value));
  }

  public void save() throws IOException {
    File pluginFile = Configurations.getPluginFile(getFileName());
    getFile().save(pluginFile);
  }

  public YamlConfiguration getFile() {
    try {
      return PlayerSettingsProvider.getPlugin()
          .getConfigurationManager()
          .getConfiguration(this.getFileName());
    } catch (ExecutionException e) {
      PlayerSettingsProvider.getPlugin()
          .getLogger()
          .severe("An exception occurred wile reading configuration '" + this.getFileName() + "':");
      e.printStackTrace();
      return null;
    }
  }
}
