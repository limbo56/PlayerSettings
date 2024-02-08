package me.limbo56.playersettings.lib;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.util.PluginLogger;
import net.byteflux.libby.BukkitLibraryManager;

public class LibraryManager {
  private final PlayerSettings plugin;

  public LibraryManager(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  public void loadLibraries() {
    PluginLogger.info("Loading libraries...");

    // Load dependency libraries
    BukkitLibraryManager bukkitLibraryManager = new BukkitLibraryManager(plugin);
    bukkitLibraryManager.addMavenCentral();
    for (Libraries library : Libraries.values()) {
      bukkitLibraryManager.loadLibrary(library.toLibrary());
    }
  }
}
