package me.limbo56.playersettings;

public final class PlayerSettingsProvider {
  private static PlayerSettings plugin;

  public static PlayerSettings getPlugin() {
    return plugin;
  }

  public static void setPlugin(PlayerSettings plugin) {
    PlayerSettingsProvider.plugin = plugin;
  }
}
