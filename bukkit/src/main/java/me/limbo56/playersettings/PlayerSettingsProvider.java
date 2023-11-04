package me.limbo56.playersettings;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class PlayerSettingsProvider {
  private static PlayerSettings plugin;
  private static BukkitAudiences adventure;

  public static PlayerSettings getPlugin() {
    return plugin;
  }

  public static void setPlugin(PlayerSettings plugin) {
    PlayerSettingsProvider.plugin = plugin;
  }

  public static @NotNull BukkitAudiences adventure() {
    if (adventure == null) {
      throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
    }
    return adventure;
  }

  public static void registerAdventure(JavaPlugin plugin) {
    adventure = BukkitAudiences.create(plugin);
  }

  public static void unregisterAdventure() {
    if (adventure != null) {
      adventure.close();
      adventure = null;
    }
  }
}
