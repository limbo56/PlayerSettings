package me.lim_bo56.lnpp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.lim_bo56.lnpp.MainPreferences;

/**
 * 
 * @author lim_bo56
 *
 */

public class UpdateChecker {

	private MainPreferences plugin = MainPreferences.getInstance();
	private static UpdateChecker instance = new UpdateChecker();
	
	public static UpdateChecker getInstance() {
		return instance;
	}
	
	/**
	 * Credits to @Maximvdw
	 * For making this method.
	 * @Maximvdw: https://www.spigotmc.org/members/maximvdw.6687/
	 */
	public String checkForUpdate() {
		try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + "14622")
                            .getBytes("UTF-8"));
            String NewVersion = new BufferedReader(new InputStreamReader(
                    con.getInputStream())).readLine().replaceAll("[a-zA-Z ]", "");
            String OldVersion = plugin.getDescription().getVersion();
            if (!NewVersion.equals(OldVersion)) {
                return "§dNew version available §b" + NewVersion + "\n" + "§aDownload§f>§7> §fhttps://goo.gl/853ZVt";    
            }
        } catch (Exception ex) {
            plugin.getLogger().info("Failed to check for a update on spigot.");
        }
		return "§dNo updates avialable at this time.";
	}
}
