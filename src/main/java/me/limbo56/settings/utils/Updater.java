package me.limbo56.settings.utils;

import me.limbo56.settings.PlayerSettings;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:27:48 AM
 */
public class Updater {

    private static PlayerSettings plugin = PlayerSettings.getInstance();

    /**
     * Method to check current updates
     * Credits to @Maximvdw
     *
     * @param player The player which the message will be sent to
     */
    static void sendUpdater(Player player) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=14622").getBytes("UTF-8"));

            String spigotVersionString = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine().replaceAll("[a-zA-Z ]", "");

            int spigotVersion = Integer.valueOf(spigotVersionString.replace(".", ""));
            int actualVersion = Integer.valueOf(plugin.getDescription().getVersion().replace(".", ""));

            if (spigotVersion > actualVersion) {
                player.sendMessage(ColorUtils.Color(Cache.CHAT_TITLE +
                        "&dNew version available &b" + spigotVersionString + "\n"
                        + "&aDownload&f>&7> &fhttp://bit.ly/PlayerSettings"));
            }
        } catch (Exception ex) {
            player.sendMessage(ColorUtils.Color(Cache.CHAT_TITLE + "&cFailed to check for an update on spigot."));
        }
    }

    /**
     * Method to check current updates
     * Credits to @Maximvdw
     */
    public static void sendUpdater() {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=14622").getBytes("UTF-8"));

            String spigotVersionString = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine().replaceAll("[a-zA-Z ]", "");

            int spigotVersion = Integer.valueOf(spigotVersionString.replace(".", ""));
            int actualVersion = Integer.valueOf(plugin.getDescription().getVersion().replace(".", ""));

            if (spigotVersion > actualVersion) {
                plugin.log("New version available " + spigotVersionString);
                plugin.log("Download: http://bit.ly/PlayerSettings");
            } else {
                plugin.log("Plugin is up to date, No updates available at this time.");
            }
        } catch (Exception ex) {
            plugin.log("Failed to check for an update on spigot.");
        }
    }
}
