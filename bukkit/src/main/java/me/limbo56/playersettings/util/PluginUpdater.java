package me.limbo56.playersettings.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginUpdater {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private static final String PREFIX = "&f&l(&6PlayerSettings&f&l) &f";
  private static final String API_URL = "https://api.spigotmc.org/legacy/update.php?resource=%s";
  private static final String PLUGIN_ID = "14622";
  private static final AtomicReference<String> latestVersion = new AtomicReference<>();
  private static final AtomicReference<Instant> lastUpdated = new AtomicReference<>();

  public static void sendUpdateMessage(Player player) {
    getOrRefreshUpdateMessage(message -> Text.from(message).sendMessage(player, PREFIX));
  }

  public static void logUpdateMessage() {
    getOrRefreshUpdateMessage(message -> PluginLogHandler.log(Colors.translateColorCodes(message)));
  }

  private static void getOrRefreshUpdateMessage(Consumer<String> consumer) {
    Instant lastUpdated = PluginUpdater.lastUpdated.get();
    if (lastUpdated == null || Instant.now().isAfter(lastUpdated.plus(15, ChronoUnit.MINUTES))) {
      Bukkit.getScheduler()
          .runTaskAsynchronously(
              PLUGIN,
              () -> {
                refreshPluginVersion();
                Bukkit.getScheduler().runTask(PLUGIN, () -> consumer.accept(getUpdateMessage()));
              });
      return;
    }

    consumer.accept(getUpdateMessage());
  }

  private static String getUpdateMessage() {
    String latestVersionNumber = PluginUpdater.latestVersion.get();
    if (latestVersion.equals("ERROR")) {
      return "&cAn error occurred while checking for updates!";
    }

    String currentVersionNumber = PLUGIN.getDescription().getVersion();
    Version latestVersion = Version.from(latestVersionNumber);
    Version currentVersion = Version.from(currentVersionNumber);
    if (currentVersion.isOlderThan(latestVersion)) {
      return String.format("New version available &av%s", latestVersionNumber);
    } else if (latestVersion.isOlderThan(currentVersion)) {
      return String.format("Running unknown version &cv%s", currentVersionNumber);
    } else {
      return String.format("Running version &6v%s", currentVersionNumber);
    }
  }

  private static void refreshPluginVersion() {
    String version = fetchPluginVersion();
    latestVersion.set(version);
    lastUpdated.set(Instant.now());
  }

  private static String fetchPluginVersion() {
    try {
      String url = String.format(API_URL, PLUGIN_ID);
      try (InputStream inputStream = new URL(url).openStream();
          BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        return reader.readLine();
      }
    } catch (IOException exception) {
      exception.printStackTrace();
      return "ERROR";
    }
  }
}
