package me.limbo56.playersettings.menu.holder;

import com.google.common.cache.CacheLoader;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.util.Colors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class MenuHolderLoader extends CacheLoader<UUID, MenuHolder> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public @NotNull MenuHolder load(@NotNull UUID uuid) {
    ConfigurationSection menuSection =
        PLUGIN.getPluginConfiguration().getFile().getConfigurationSection("menu");
    if (menuSection == null) {
      throw new NullPointerException(
          "The 'menu' configuration section is from the 'menu.yml' configuration file");
    }

    String menuName = Colors.translateColorCodes(menuSection.getString("name"));
    int menuSize = menuSection.getInt("size");

    return new MenuHolder(uuid, menuName, menuSize);
  }
}
