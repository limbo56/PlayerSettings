package me.limbo56.playersettings.util;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import me.limbo56.playersettings.PlayerSettings;

public class PluginUpdater {
  private static final PlayerSettings PLUGIN = PlayerSettings.getInstance();
  public static final String PREFIX = "&f&l(&6PlayerSettings&f&l) &f";
  private static final String API_URL = "https://api.spigotmc.org/legacy/update.php?resource=%s";
  private static final String PLUGIN_ID = "14622";
  private static final AtomicReference<String> LATEST_VERSION = new AtomicReference<>();
  private static final AtomicReference<Instant> LAST_UPDATED = new AtomicReference<>();

  private PluginUpdater() {}

  public static CompletableFuture<List<String>> getVersionMessage() {
    return PluginUpdater.getLatestVersion()
        .thenApplyAsync(PluginUpdater::composeVersionMessage)
        .exceptionally(
            exception -> {
              PluginLogger.severe("An error occurred while checking for updates", exception);
              return Collections.singletonList("&cAn error occurred while checking for updates!");
            });
  }

  private static List<String> composeVersionMessage(String version) {
    String currentVersionNumber = PLUGIN.getDescription().getVersion();
    Version latestVersion = Version.from(version);
    Version currentVersion = Version.from(currentVersionNumber);

    if (version.equals("ERROR")) {
      return Collections.singletonList("&cAn error occurred while checking for updates!");
    } else if (currentVersion.isOlderThan(latestVersion)) {
      return Lists.newArrayList(
          "New version available &av" + version + " &r(Using &av" + currentVersionNumber + "&r)",
          "Download the latest update at:",
          "&6https://www.spigotmc.org/resources/player-settings.14622/");
    } else if (latestVersion.isOlderThan(currentVersion)) {
      return Collections.singletonList("Using unknown version &cv" + currentVersionNumber);
    } else {
      return Collections.singletonList("Using latest version &6v" + currentVersionNumber);
    }
  }

  public static CompletableFuture<String> getLatestVersion() {
    return CompletableFuture.supplyAsync(PluginUpdater::refreshAndGetVersion);
  }

  private static String refreshAndGetVersion() {
    if (shouldRefreshPluginVersion()) {
      refreshPluginVersion();
    }
    return LATEST_VERSION.get();
  }

  private static boolean shouldRefreshPluginVersion() {
    Instant lastUpdated = LAST_UPDATED.get();
    return lastUpdated == null || Instant.now().isAfter(lastUpdated.plus(15, ChronoUnit.MINUTES));
  }

  private static void refreshPluginVersion() {
    try (InputStream inputStream = new URL(String.format(API_URL, PLUGIN_ID)).openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String version = reader.readLine();
      LATEST_VERSION.set(version);
      LAST_UPDATED.set(Instant.now());
    } catch (IOException exception) {
      PluginLogger.severe(
          "An exception occurred while checking refreshing the plugin version", exception);
      LATEST_VERSION.set("ERROR");
    }
  }
}
