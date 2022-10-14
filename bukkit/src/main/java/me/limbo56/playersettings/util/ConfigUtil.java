package me.limbo56.playersettings.util;

import java.io.File;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public final class ConfigUtil {
  /**
   * Gets a new {@link File} instance using given file name from the plugin's data folder.
   *
   * @param fileName The name of the file to get.
   * @return {@link File} instance.
   */
  @NotNull
  public static File getPluginFile(String fileName) {
    return new File(PlayerSettingsProvider.getPlugin().getDataFolder(), fileName);
  }

  /**
   * Writes a {@link ConfigurationSerializable} to a given {@code path} in a {@link
   * YamlConfiguration}.
   *
   * @param file File to write object to.
   * @param path Path to write object at.
   * @param serializable {@link ConfigurationSerializable} object to write.
   */
  public static void configureSerializable(
      YamlConfiguration file, String path, ConfigurationSerializable serializable) {
    serializable.serialize().forEach((key, value) -> file.set(path + "." + key, value));
  }
}
