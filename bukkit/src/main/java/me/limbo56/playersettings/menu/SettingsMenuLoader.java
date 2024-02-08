package me.limbo56.playersettings.menu;

import com.google.common.cache.CacheLoader;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.util.Colors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class SettingsMenuLoader extends CacheLoader<UUID, SettingsMenu> {
  private final PlayerSettings plugin;

  public SettingsMenuLoader(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  @Override
  public @NotNull SettingsMenu load(@NotNull UUID uuid) {
    ConfigurationSection menuSection = getMenuSection();
    String menuName = Colors.translateColorCodes(menuSection.getString("name"));
    int menuSize = menuSection.getInt("size");
    return new SettingsMenu(uuid, menuName, menuSize);
  }

  @NotNull
  private ConfigurationSection getMenuSection() {
    ConfigurationSection menuSection =
        plugin.getConfiguration().getFile().getConfigurationSection("menu");
    if (menuSection == null) {
      throw new NullPointerException(
          "The 'menu' configuration section is missing from the 'config.yml' configuration file");
    }
    return menuSection;
  }
}
