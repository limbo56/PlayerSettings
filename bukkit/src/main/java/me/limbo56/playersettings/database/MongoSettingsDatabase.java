package me.limbo56.playersettings.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCompressor;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.database.configuration.MongoDatabaseConfiguration;
import me.limbo56.playersettings.user.UserSettingsWatcher;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.conversions.Bson;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class MongoSettingsDatabase implements SettingsDatabase<MongoDatabaseConfiguration> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final MongoDatabaseConfiguration databaseConfiguration;
  private MongoClient client;

  public MongoSettingsDatabase(ConfigurationSection configuration) {
    this.databaseConfiguration = new MongoDatabaseConfiguration(configuration);
  }

  @Override
  public void connect() {
    this.client = getMongoClient();

    // Create indexes
    MongoCollection<Document> settingsCollection = getSettingsCollection(client);
    settingsCollection.createIndex(
        Indexes.descending("settingName", "owner"), new IndexOptions().unique(true));
  }

  @Override
  public void disconnect() {
    if (this.client == null) return;
    this.client.close();
  }

  @Override
  public Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> uuids) {
    Map<UUID, SettingWatcher> watcherMap = new HashMap<>();
    Bson filter =
        and(in("owner", uuids), in("settingName", PLUGIN.getSettingsManager().getSettingNames()));
    for (Document settingDocument : getSettingsCollection(client).find(filter)) {
      UUID owner = settingDocument.get("owner", UUID.class);
      SettingWatcher watcher = watcherMap.getOrDefault(owner, new UserSettingsWatcher(owner));
      watcher.setValue(
          settingDocument.getString("settingName"), settingDocument.getInteger("value"), true);
      watcherMap.put(owner, watcher);
    }
    return watcherMap.values();
  }

  @Override
  public void saveSettingWatchers(Collection<SettingWatcher> settingWatchers) {
    MongoCollection<Document> settingsCollection = getSettingsCollection(client);
    UpdateOptions options = new UpdateOptions().upsert(true);
    for (SettingWatcher settingWatcher : settingWatchers) {
      for (String settingName : settingWatcher.getWatched()) {
        Bson filter = and(eq("owner", settingWatcher.getOwner()), eq("settingName", settingName));
        Bson update = Updates.set("value", settingWatcher.getValue(settingName));
        settingsCollection.updateOne(filter, update, options);
      }
    }
  }

  @Override
  public void putExtra(UUID uuid, Setting setting, String key, String value) {
    MongoCollection<Document> extraCollection = getSettingsCollection(client);
    Bson filter = and(eq("owner", uuid), eq("settingName", setting.getName()));
    Bson update = Updates.set("extra." + key, value);
    extraCollection.updateOne(filter, update, new UpdateOptions().upsert(true));
  }

  @Override
  public String getExtra(UUID uuid, Setting setting, String key) {
    MongoCollection<Document> extraCollection = getSettingsCollection(client);
    Bson filter = and(eq("owner", uuid), eq("settingName", setting.getName()));
    return extraCollection
        .find(filter)
        .map(document -> document.get("extra", Document.class).getString(key))
        .first();
  }

  @Override
  public MongoDatabaseConfiguration getDatabaseConfiguration() {
    return databaseConfiguration;
  }

  private MongoClient getMongoClient() {
    ConnectionString connectionURI =
        new ConnectionString(getDatabaseConfiguration().getConnectionURI());
    return MongoClients.create(
        MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .compressorList(Collections.singletonList(MongoCompressor.createSnappyCompressor()))
            .applyConnectionString(connectionURI)
            .build());
  }

  private MongoCollection<Document> getSettingsCollection(MongoClient client) {
    return client
        .getDatabase(getDatabaseConfiguration().getDatabaseName())
        .getCollection("playersettings_settings");
  }
}
