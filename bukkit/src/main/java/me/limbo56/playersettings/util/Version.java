package me.limbo56.playersettings.util;

import org.bukkit.Bukkit;

public final class Version {
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

  public static Version getCurrentVersion() {
    return Version.from(Bukkit.getBukkitVersion());
  }

  private String parseVersion(String version) {
    return version.split(this.separator)[0];
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
