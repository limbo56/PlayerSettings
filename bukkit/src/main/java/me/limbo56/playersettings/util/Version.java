package me.limbo56.playersettings.util;

import java.util.regex.Pattern;
import org.bukkit.Bukkit;

public final class Version {
  private static final Pattern VERSION_PATTERN = Pattern.compile("\\b\\d+\\.\\d+\\.\\d+\\b");
  private static Version CURRENT_SERVER_VERSION;
  private final String version;
  private final String separator;

  public Version(String version) {
    this(version, "-");
  }

  public Version(String version, String separator) {
    this.separator = separator;
    this.version = parseVersion(version);
  }

  public static Version from(String version) {
    return new Version(version);
  }

  public static Version from(String version, String separator) {
    return new Version(version, separator);
  }

  public static Version getServerVersion() {
    if (CURRENT_SERVER_VERSION == null) {
      CURRENT_SERVER_VERSION = resolveServerVersion();
    }
    return CURRENT_SERVER_VERSION;
  }

  private static Version resolveServerVersion() {
    PluginLogger.debug("Resolving server version...");

    String bukkitVersion = Bukkit.getBukkitVersion();
    PluginLogger.debug("Testing bukkit version: " + bukkitVersion);
    if (VERSION_PATTERN.matcher(bukkitVersion).find()) {
      PluginLogger.debug("Using bukkit version: " + bukkitVersion);
      return Version.from(bukkitVersion);
    }

    String serverVersion = Bukkit.getVersion();
    PluginLogger.debug("Testing server version: " + serverVersion);
    if (serverVersion.contains("MC:") && serverVersion.contains("(")) {
      String parsedVersion = serverVersion.substring(serverVersion.indexOf('('));
      parsedVersion = parsedVersion.split(" ")[1];
      parsedVersion = parsedVersion.substring(0, parsedVersion.length() - 1);
      PluginLogger.debug("Parsed server version: " + parsedVersion);

      if (VERSION_PATTERN.matcher(parsedVersion).find()) {
        PluginLogger.debug("Using server version: " + parsedVersion);
        return Version.from(parsedVersion);
      }
    }

    PluginLogger.debug("Using fallback version: 1.8.8");
    return Version.from("1.8.8");
  }

  public boolean isOlderThan(Version version) {
    return compareTo(version) < 0;
  }

  public boolean isOlderThan(String version) {
    return compareTo(version) < 0;
  }

  public int compareTo(Version version) {
    return compareVersions(this.version, version.version);
  }

  public int compareTo(String version) {
    return compareVersions(this.version, parseVersion(version));
  }

  private String parseVersion(String version) {
    return version.split(this.separator)[0];
  }

  private int compareVersions(String current, String target) {
    String[] splitCurrent = current.split("\\.");
    String[] splitTarget = target.split("\\.");
    int i = 0;
    while (i < splitCurrent.length && i < splitTarget.length) {
      int currentValue = Integer.parseInt(splitCurrent[i]);
      int targetValue = Integer.parseInt(splitTarget[i]);
      if (currentValue > targetValue) {
        return 1;
      } else if (currentValue < targetValue) {
        return -1;
      }
      i++;
    }
    return i < splitCurrent.length ? 1 : i < splitTarget.length ? -1 : 0;
  }
}
