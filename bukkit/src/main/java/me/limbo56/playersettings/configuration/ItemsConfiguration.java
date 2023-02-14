package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.Setting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ItemsConfiguration extends BaseConfiguration {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public void writeMenuItem(String path, MenuItem menuItem) {
    try {
      this.writeSerializable(path, menuItem);
      this.save();
    } catch (IOException e) {
      PLUGIN.getLogger().severe("Failed to save item '" + path + "' to configuration");
      e.printStackTrace();
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
