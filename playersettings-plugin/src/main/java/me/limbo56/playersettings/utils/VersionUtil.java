package me.limbo56.playersettings.utils;

import org.bukkit.Bukkit;

public class VersionUtil {

    private static final String SERVER_VERSION;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        SERVER_VERSION = packageName.substring(packageName.lastIndexOf(".") + 1);
    }

    public static boolean isVersion(String version) {
        return SERVER_VERSION.equals(version);
    }

    public static boolean isGreaterThan(String targetVersion) {
        return parseVersionNumber(SERVER_VERSION) > parseVersionNumber(targetVersion);
    }

    private static int parseVersionNumber(String version) {
        return Integer.parseInt(sanitizeVersion(version));
    }

    private static String sanitizeVersion(String version) {
        return version
                .replace("_", "")
                .replace("R", "")
                .replace("v", "");
    }
}
