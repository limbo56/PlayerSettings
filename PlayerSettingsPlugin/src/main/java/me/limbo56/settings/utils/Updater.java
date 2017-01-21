package me.limbo56.settings.utils;

import me.limbo56.settings.PlayerSettings;

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
     * Method to check current updates.
     * Credits to Maximvdw.
     *
     * @return String
     */
    public static String playerUpdater() {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php")
                    .openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + "14622")
                            .getBytes("UTF-8"));
            String NewVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine()
                    .replaceAll("[a-zA-Z ]", "");
            String OldVersion = plugin.getDescription().getVersion();
            if (!NewVersion.equals(OldVersion)) {
                return ColorUtils.Color(Cache.CHAT_TITLE + "&dNew version available &b" + NewVersion + "\n" + "&aDownload&f>&7> &fhttp://bit.ly/PlayerSettings");
            }
        } catch (Exception ex) {
            plugin.getLogger().info("Failed to check for a update on spigot.");
        }
        return ColorUtils.Color(Cache.CHAT_TITLE + "&dNo updates avialable at this time.");
    }

    /**
     * Method to check current updates.
     * Credits to Maximvdw.
     *
     * @return String
     */
    public static String consoleUpdater() {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php")
                    .openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + "14622")
                            .getBytes("UTF-8"));
            String NewVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine()
                    .replaceAll("[a-zA-Z ]", "");
            String OldVersion = plugin.getDescription().getVersion();
            if (!NewVersion.equals(OldVersion)) {
                return "New version available " + NewVersion;
            }
        } catch (Exception ex) {
            plugin.getLogger().info("Failed to check for a update on spigot.");
        }
        return "No updates avialable at this time.";
    }

}
