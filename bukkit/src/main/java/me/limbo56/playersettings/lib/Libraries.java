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
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  ADVENTURE(
      Library.builder()
          .id("adventure")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-bukkit")
          .version("4.3.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_API(
      Library.builder()
          .id("adventure_api")
          .groupId("net{}kyori")
          .artifactId("adventure-api")
          .version("4.13.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_KEY(
      Library.builder()
          .id("adventure_key")
          .groupId("net{}kyori")
          .artifactId("adventure-key")
          .version("4.13.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_NBT(
      Library.builder()
          .id("adventure_nbt")
          .groupId("net{}kyori")
          .artifactId("adventure-nbt")
          .version("4.13.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_PLATFORM_API(
      Library.builder()
          .id("adventure_platform_api")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-api")
          .version("4.3.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_PLATFORM_BUKKIT(
      Library.builder()
          .id("adventure_platform_bukkit")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-bukkit")
          .version("4.3.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_PLATFORM_FACET(
      Library.builder()
          .id("adventure_platform_facet")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-facet")
          .version("4.3.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_PLATFORM_VIAVERSION(
      Library.builder()
          .id("adventure_platform_viaversion")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-viaversion")
          .version("4.3.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_TEXT_SERIALIZER_BUNGEECORD(
      Library.builder()
          .id("adventure_text_serializer_bungeecord")
          .groupId("net{}kyori")
          .artifactId("adventure-text-serializer-bungeecord")
          .version("4.3.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_TEXT_SERIALIZER_LEGACY(
      Library.builder()
          .id("adventure_text_serializer_legacy")
          .groupId("net{}kyori")
          .artifactId("adventure-text-serializer-legacy")
          .version("4.13.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_TEXT_SERIALIZER_GSON_LEGACY_IMPL(
      Library.builder()
          .id("adventure_text_serializer_legacy_gson_impl")
          .groupId("net{}kyori")
          .artifactId("adventure-text-serializer-gson-legacy-impl")
          .version("4.13.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_TEXT_SERIALIZER_GSON(
      Library.builder()
          .id("adventure_text_serializer_gson")
          .groupId("net{}kyori")
          .artifactId("adventure-text-serializer-gson")
          .version("4.13.1")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_EXAMINATION_API(
      Library.builder()
          .id("adventure_examination_api")
          .groupId("net{}kyori")
          .artifactId("examination-api")
          .version("1.3.0")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_EXAMINATION_STRING(
      Library.builder()
          .id("adventure_examination_api")
          .groupId("net{}kyori")
          .artifactId("examination-string")
          .version("1.3.0")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori"));

  private final Library.Builder libraryBuilder;

  Libraries(Library.Builder libraryBuilder) {
    this.libraryBuilder = libraryBuilder;
  }

  public Library toLibrary() {
    return libraryBuilder.build();
  }
}
