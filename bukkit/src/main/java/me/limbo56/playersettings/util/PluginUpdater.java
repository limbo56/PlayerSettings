package me.limbo56.playersettings.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class PluginUpdater {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private static final String PREFIX = "&f&l(&6PlayerSettings&f&l) &f";
  private static final String API_URL = "https://api.spigotmc.org/legacy/update.php?resource=%s";
  private static final String PLUGIN_ID = "14622";

  public static void sendUpdateMessage(Player player) {
    fetchAsync(
        PluginUpdater::getUpdateMessage, message -> Text.from(message).sendMessage(player, PREFIX));
  }

  public static void logUpdateMessage() {
    fetchAsync(
        PluginUpdater::getUpdateMessage,
        message -> plugin.getLogger().info(ColorUtil.translateColorCodes(message)));
  }

  private static <T> void fetchAsync(Supplier<T> collector, Consumer<T> consumer) {
    BukkitScheduler scheduler = Bukkit.getScheduler();
    scheduler.runTaskAsynchronously(
        plugin,
        () -> {
          T collected = collector.get();
          scheduler.runTask(plugin, () -> consumer.accept(collected));
        });
  }

  private static String getUpdateMessage() {
    try {
      String latestVersionText = fetchLatestPluginVersion();
      int latestVersion = Integer.parseInt(latestVersionText.replaceAll("\\.", ""));

      String currentVersionText = plugin.getDescription().getVersion();
      int currentVersion = Integer.parseInt(currentVersionText.replaceAll("\\.", ""));

      if (latestVersion > currentVersion) {
        return String.format("New version available &av%s", latestVersionText);
      } else if (currentVersion > latestVersion) {
        return String.format("Running unknown version &cv%s", currentVersionText);
      } else {
        return String.format("Running version &6v%s", currentVersionText);
      }
    } catch (IOException e) {
      return "&cAn error occurred while checking for updates!";
    }
  }

  private static String fetchLatestPluginVersion() throws IOException {
    String url = String.format(API_URL, PLUGIN_ID);
    try (InputStream inputStream = new URL(url).openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      return reader.readLine();
    }
  }
}
