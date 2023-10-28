package me.limbo56.playersettings.lib;

import net.byteflux.libby.Library;

public enum Libraries {
  HIKARI(
      Library.builder()
          .id("hikari_pool")
          .groupId("com{}zaxxer")
          .artifactId("HikariCP")
          .version("4.0.3")
          .relocate("com{}zaxxer{}hikari", "me{}limbo56{}playersettings{}lib{}hikari")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  MYSQL(
      Library.builder()
          .id("mysql_driver")
          .groupId("com{}mysql")
          .artifactId("mysql-connector-j")
          .version("8.1.0")
          .relocate("com{}mysql", "me{}limbo56{}playersettings{}lib{}mysql")),
  SQLITE(
      Library.builder()
          .id("sqlite_driver")
          .groupId("org{}xerial")
          .artifactId("sqlite-jdbc")
          .version("3.42.0.0")
          .relocate("org{}sqlite", "me{}limbo56{}playersettings{}lib{}sqlite")),
  MONGODB(
      Library.builder()
          .id("mongodb_driver")
          .groupId("org{}mongodb")
          .artifactId("mongo-java-driver")
          .version("3.12.14")
          .relocate("org{}mongodb", "me{}limbo56{}playersettings{}lib{}mongodb")
          .relocate("org{}xerial{}snappy", "me{}limbo56{}playersettings{}lib{}snappy")),
  SNAPPY(
      Library.builder()
          .id("snappy_compression")
          .groupId("org{}xerial{}snappy")
          .artifactId("snappy-java")
          .version("1.1.10.4")
          .relocate("org{}xerial{}snappy", "me{}limbo56{}playersettings{}lib{}snappy")),
  SLF4J_API(
      Library.builder()
          .id("slf4j_api")
          .groupId("org{}slf4j")
          .artifactId("slf4j-api")
          .version("2.0.5")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  SLF4J_SIMPLE(
      Library.builder()
          .id("slf4j_api")
          .groupId("org{}slf4j")
          .artifactId("slf4j-simple")
          .version("2.0.5")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j"));

  private final Library.Builder libraryBuilder;

  Libraries(Library.Builder libraryBuilder) {
    this.libraryBuilder = libraryBuilder;
  }

  public Library toLibrary() {
    return libraryBuilder.build();
  }
}
