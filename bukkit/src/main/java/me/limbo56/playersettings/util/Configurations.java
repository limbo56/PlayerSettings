package me.limbo56.playersettings.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import me.limbo56.playersettings.PlayerSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.jetbrains.annotations.NotNull;

/** Utility class for working with plugin configurations. */
public class Configurations {
  private Configurations() {}

  /**
   * Gets a {@link File} instance for the specified file name within the plugin's data folder.
   *
   * @param fileName The name of the file to retrieve.
   * @return A {@link File} instance representing the specified file within the plugin's data
   *     folder.
   */
  @NotNull
  public static File getPluginFile(String fileName) {
    return new File(PlayerSettings.getInstance().getDataFolder(), fileName);
  }

  /**
   * Converts a {@link ConfigurationSection} to a {@link Map} representation recursively.
   *
   * @param section The {@link ConfigurationSection} to convert.
   * @return A {@link Map} representing the contents of the configuration section.
   */
  @NotNull
  public static Map<String, Object> mapSection(@NotNull ConfigurationSection section) {
    Map<String, Object> map = new HashMap<>();
    for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
      Object value = entry.getValue();
      if (value instanceof MemorySection) {
        value = mapSection((ConfigurationSection) value);
      }
      map.put(entry.getKey(), value);
    }
    return map;
  }
}
