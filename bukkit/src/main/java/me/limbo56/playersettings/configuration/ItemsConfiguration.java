package me.limbo56.playersettings.configuration;

import java.io.IOException;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.util.PluginLogger;
import org.jetbrains.annotations.NotNull;

public class ItemsConfiguration extends BaseConfiguration {
  public ItemsConfiguration(PlayerSettings plugin) {
    super(plugin);
  }

  @Override
  @NotNull
  public String getFileName() {
    return "items.yml";
  }

  public void writeMenuItem(String path, MenuItem menuItem) {
    try {
      writeSerializable(path, menuItem);
      save();
    } catch (IOException exception) {
      PluginLogger.severe("Failed to save item '" + path + "' to configuration", exception);
    }
  }

  public boolean isItemConfigured(String itemName) {
    return configuration.contains(itemName);
  }

  public boolean isGlowEnabled(Setting setting) {
    return configuration.getBoolean(setting.getName() + ".glow", true);
  }
}
