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
          .version("8.2.0")
          .relocate("com{}mysql", "me{}limbo56{}playersettings{}lib{}mysql")),
  SQLITE(
      Library.builder()
          .id("sqlite_driver")
          .groupId("org{}xerial")
          .artifactId("sqlite-jdbc")
          .version("3.45.0.0")
          .relocate("org{}sqlite", "me{}limbo56{}playersettings{}lib{}sqlite")),
  BSON(
      Library.builder()
          .id("bson")
          .groupId("org{}mongodb")
          .artifactId("bson")
          .version("4.10.2")
          .relocate("org{}bson", "me{}limbo56{}playersettings{}lib{}bson")
          .relocate("org{}mongodb", "me{}limbo56{}playersettings{}lib{}mongodb")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  BSON_RECORD_CODEC(
      Library.builder()
          .id("bson_record_codec")
          .groupId("org{}mongodb")
          .artifactId("bson-record-codec")
          .version("4.10.2")
          .relocate("org{}bson", "me{}limbo56{}playersettings{}lib{}bson")
          .relocate("org{}mongodb", "me{}limbo56{}playersettings{}lib{}mongodb")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  MONGODB_DRIVER_LEGACY(
      Library.builder()
          .id("mongodb_driver_legacy")
          .groupId("org{}mongodb")
          .artifactId("mongodb-driver-legacy")
          .version("4.10.2")
          .relocate("org{}mongodb", "me{}limbo56{}playersettings{}lib{}mongodb")
          .relocate("org{}bson", "me{}limbo56{}playersettings{}lib{}bson")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  MONGODB_DRIVER_CORE(
      Library.builder()
          .id("mongodb_driver_core")
          .groupId("org{}mongodb")
          .artifactId("mongodb-driver-core")
          .version("4.10.2")
          .relocate("org{}mongodb", "me{}limbo56{}playersettings{}lib{}mongodb")
          .relocate("org{}bson", "me{}limbo56{}playersettings{}lib{}bson")
          .relocate("org{}xerial{}snappy", "me{}limbo56{}playersettings{}lib{}snappy")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  MONGODB_DRIVER_SYNC(
      Library.builder()
          .id("mongodb_driver_core")
          .groupId("org{}mongodb")
          .artifactId("mongodb-driver-sync")
          .version("4.10.2")
          .relocate("org{}mongodb", "me{}limbo56{}playersettings{}lib{}mongodb")
          .relocate("org{}bson", "me{}limbo56{}playersettings{}lib{}bson")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  SNAPPY(
      Library.builder()
          .id("snappy_compression")
          .groupId("org{}xerial{}snappy")
          .artifactId("snappy-java")
          .version("1.1.10.5")
          .relocate("org{}xerial{}snappy", "me{}limbo56{}playersettings{}lib{}snappy")),
  SLF4J_API(
      Library.builder()
          .id("slf4j_api")
          .groupId("org{}slf4j")
          .artifactId("slf4j-api")
          .version("1.7.6")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  SLF4J_SIMPLE(
      Library.builder()
          .id("slf4j_api")
          .groupId("org{}slf4j")
          .artifactId("slf4j-simple")
          .version("1.7.6")
          .relocate("org{}slf4j", "me{}limbo56{}playersettings{}lib{}slf4j")),
  ADVENTURE(
      Library.builder()
          .id("adventure")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-bukkit")
          .version("4.3.2")
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
          .version("4.3.2")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_PLATFORM_BUKKIT(
      Library.builder()
          .id("adventure_platform_bukkit")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-bukkit")
          .version("4.3.2")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_PLATFORM_FACET(
      Library.builder()
          .id("adventure_platform_facet")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-facet")
          .version("4.3.2")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_PLATFORM_VIAVERSION(
      Library.builder()
          .id("adventure_platform_viaversion")
          .groupId("net{}kyori")
          .artifactId("adventure-platform-viaversion")
          .version("4.3.2")
          .relocate("net{}kyori", "me{}limbo56{}playersettings{}lib{}kyori")),
  ADVENTURE_TEXT_SERIALIZER_BUNGEECORD(
      Library.builder()
          .id("adventure_text_serializer_bungeecord")
          .groupId("net{}kyori")
          .artifactId("adventure-text-serializer-bungeecord")
          .version("4.3.2")
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
