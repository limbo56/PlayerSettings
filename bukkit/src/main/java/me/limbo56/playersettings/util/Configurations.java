package me.limbo56.playersettings.util;

import java.io.File;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.jetbrains.annotations.NotNull;

public final class Configurations {
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
}
