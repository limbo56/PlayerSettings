package me.limbo56.playersettings.configuration;

import java.util.concurrent.ExecutionException;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.configuration.file.YamlConfiguration;

public interface PluginConfiguration {
  String getFileName();

  default YamlConfiguration getFile() {
    try {
      return PlayerSettingsProvider.getPlugin()
          .getConfigurationManager()
          .getConfiguration(this.getFileName());
    } catch (ExecutionException e) {
      PlayerSettingsProvider.getPlugin()
          .getLogger()
          .severe("Failed to read configuration '" + this.getFileName() + "'");
      e.printStackTrace();
      return null;
    }
  }
}
