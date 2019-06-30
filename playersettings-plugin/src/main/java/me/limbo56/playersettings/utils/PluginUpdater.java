package me.limbo56.playersettings.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.limbo56.playersettings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class PluginUpdater {
    private static final Plugin PLUGIN = PlayerSettings.getPlugin();
    private static final String API_URL = "https://api.spiget.org/v2/resources/%s/versions/latest";
    private static final String PLUGIN_ID = "14622";
    private static final String PREFIX = "&f[&cPlayerSettings&f] ";

    public static void sendUpdateMessage(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
            String message = getUpdateMessage();

            if (message == null) return;
            PlayerUtils.sendMessage(player, PREFIX + message);
        });
    }

    public static void sendUpdateMessage() {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
            String message = getUpdateMessage();

            if (message == null) return;
            PLUGIN.getLogger().warning(ChatColor.stripColor(ColorUtils.translateString(message)));
        });
    }

    private static String getUpdateMessage() {
        try {
            JsonObject reqObject = readFromURL(String.format(API_URL, PLUGIN_ID));
            String latestVersion = reqObject.get("name").getAsString();
            String currentVersion = PLUGIN.getDescription().getVersion();
            int parsedLatest = Integer.parseInt(latestVersion.replaceAll("\\.", ""));
            int parsedCurrent = Integer.parseInt(currentVersion.replaceAll("\\.", ""));

            if (parsedLatest > parsedCurrent)
                return "New version available: &av" + latestVersion;

            if (parsedCurrent > parsedLatest)
                return "You are from the future, but we are not there yet. Consider going back the most recent version in the Spigot page.";

            return null;
        } catch (IOException e) {
            PLUGIN.getLogger().warning("Failed to check for updates. Check your connection!");
            return null;
        }
    }

    private static JsonObject readFromURL(String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String jsonText = readAll(reader);
            return new JsonParser().parse(jsonText).getAsJsonObject();
        } catch (IOException e) {
            PLUGIN.getLogger().warning("Failed to check for updates. Check your connection!");
            return null;
        }
    }

    private static String readAll(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
