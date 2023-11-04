package me.limbo56.playersettings.configuration;

import java.io.IOException;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.util.PluginLogger;
import org.jetbrains.annotations.NotNull;

public class ItemsConfiguration extends BaseConfiguration {
  public void writeMenuItem(String path, MenuItem menuItem) {
    try {
      this.writeSerializable(path, menuItem);
      this.save();
    } catch (IOException exception) {
      PluginLogger.severe("Failed to save item '" + path + "' to configuration");
      exception.printStackTrace();
    }
  }

  public boolean isItemConfigured(String itemName) {
    return getFile().contains(itemName);
  }

  public boolean isGlowEnabled(Setting setting) {
    return getFile().getBoolean(setting.getName() + ".glow", true);
  }

  @NotNull
  public String getFileName() {
    return "items.yml";
  }
}
