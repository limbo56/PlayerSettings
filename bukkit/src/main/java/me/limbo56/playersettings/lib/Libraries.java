package me.limbo56.playersettings.lib;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.byteflux.libby.Library;

public enum Libraries implements LibraryObject {
  HIKARI(
      "com{}zaxxer",
      "HikariCP",
      "4.0.3",
      "hikari_pool",
      "com{}zaxxer{}hikari",
      "me{}limbo56{}playersettings{}lib{}hikari"),
  MYSQL(
      "com{}mysql",
      "mysql-connector-j",
      "8.1.0",
      "mysql_driver",
      "com{}mysql",
      "me{}limbo56{}playersettings{}lib{}mysql"),
  SQLITE(
      "org{}xerial",
      "sqlite-jdbc",
      "3.42.0.0",
      "sqlite_driver",
      "org{}sqlite",
      "me{}limbo56{}playersettings{}lib{}sqlite"),
  MONGODB(
      "org{}mongodb",
      "mongo-java-driver",
      "3.12.14",
      "mongodb_driver",
      ImmutableMap.of(
          "org{}mongodb",
          "me{}limbo56{}playersettings{}lib{}mongo",
          "org{}xerial{}snappy",
          "me{}limbo56{}playersettings{}lib{}snappy")),
  SNAPPY(
      "org{}xerial{}snappy",
      "snappy-java",
      "1.1.10.4",
      "snappy_compression",
      "org{}xerial{}snappy",
      "me{}limbo56{}playersettings{}lib{}snappy");

  private final String groupId;
  private final String artifactId;
  private final String version;
  private final String id;
  private final Map<String, String> relocations;

  Libraries(
      String groupId,
      String artifactId,
      String version,
      String id,
      String oldRelocation,
      String newRelocation) {
    this(groupId, artifactId, version, id, ImmutableMap.of(oldRelocation, newRelocation));
  }

  Libraries(
      String groupId,
      String artifactId,
      String version,
      String id,
      Map<String, String> relocations) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.id = id;
    this.relocations = relocations;
  }

  @Override
  public String getGroupId() {
    return groupId;
  }

  @Override
  public String getArtifactId() {
    return artifactId;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Map<String, String> getRelocations() {
    return relocations;
  }

  public Library getLibrary() {
    Library.Builder library =
        Library.builder().groupId(groupId).artifactId(artifactId).version(version).id(id);
    getRelocations().forEach(library::relocate);
    return library.build();
  }
}
