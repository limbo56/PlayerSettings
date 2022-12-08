package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.MenuItem;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class ItemsConfiguration extends BaseConfiguration {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public void configureItem(String path, MenuItem menuItem) {
    try {
      this.writeSerializable(path, menuItem);
      this.save();
    } catch (IOException e) {
      PLUGIN.getLogger().severe("Failed to save item '" + path + "' to configuration");
      e.printStackTrace();
    }
  }

  public MenuItem getMenuItem(String name) {
    ConfigurationSection itemSection =
        Objects.requireNonNull(getFile().getConfigurationSection(name));
    return MenuItem.deserialize(itemSection);
  }

  public boolean isItemConfigured(String itemName) {
    return getFile().contains(itemName);
  }

  @NotNull
  public String getFileName() {
    return "items.yml";
  }
}
