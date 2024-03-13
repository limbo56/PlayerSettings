package me.limbo56.playersettings.lib;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.util.PluginLogger;
import net.byteflux.libby.BukkitLibraryManager;

public class LibraryManager {
  private final BukkitLibraryManager libraryManager;

  public LibraryManager(PlayerSettings plugin) {
    this.libraryManager = new BukkitLibraryManager(plugin);
  }

  public void loadLibraries() {
    PluginLogger.info("Loading libraries...");
    libraryManager.addMavenCentral();
    for (Libraries library : Libraries.values()) {
      libraryManager.loadLibrary(library.toLibrary());
    }
  }
}
