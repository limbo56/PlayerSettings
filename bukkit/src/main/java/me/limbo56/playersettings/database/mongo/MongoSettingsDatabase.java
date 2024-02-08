package me.limbo56.playersettings.database.mongo;

import static com.mongodb.client.model.Filters.*;

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
import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.database.SettingsDatabase;
import me.limbo56.playersettings.user.UserSettingsWatcher;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.conversions.Bson;
import org.bukkit.configuration.ConfigurationSection;

public class MongoSettingsDatabase implements SettingsDatabase<MongoDatabaseConfiguration> {
  private final MongoDatabaseConfiguration databaseConfiguration;
  private MongoClient client;
  private MongoCollection<Document> settingsCollection;

  public MongoSettingsDatabase(ConfigurationSection configuration) {
    this.databaseConfiguration = new MongoDatabaseConfiguration(configuration);
  }

  @Override
  public void connect() {
    if (isConnected()) {
      disconnect();
    }

    // Initialize client
    this.client = createMongoClient();
    this.settingsCollection = getSettingsCollection(client);

    // Create indexes
    this.settingsCollection.createIndex(
        Indexes.descending("settingName", "owner"), new IndexOptions().unique(true));
  }

  @Override
  public void disconnect() {
    if (isConnected()) {
      this.client.close();
      this.client = null;
      this.settingsCollection = null;
    }
  }

  @Override
  public Collection<SettingWatcher> loadSettingWatchers(Collection<UUID> uuids) {
    Set<String> settingNames = PlayerSettings.getInstance().getSettingsManager().getSettingNames();
    Bson filter = and(in("owner", uuids), in("settingName", settingNames));
    Map<UUID, SettingWatcher> watcherMap = new HashMap<>();

    for (Document settingDocument : settingsCollection.find(filter)) {
      UUID owner = settingDocument.get("owner", UUID.class);
      SettingWatcher watcher = watcherMap.getOrDefault(owner, new UserSettingsWatcher(owner));

      String settingName = settingDocument.getString("settingName");
      Integer value = settingDocument.getInteger("value");
      watcher.setValue(settingName, value, true);

      watcherMap.put(owner, watcher);
    }

    return watcherMap.values();
  }

  @Override
  public void saveSettingWatchers(Collection<SettingWatcher> settingWatchers) {
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
    Bson filter = and(eq("owner", uuid), eq("settingName", setting.getName()));
    Bson update = Updates.set("extra." + key, value);
    settingsCollection.updateOne(filter, update, new UpdateOptions().upsert(true));
  }

  @Override
  public String getExtra(UUID uuid, Setting setting, String key) {
    Bson filter = and(eq("owner", uuid), eq("settingName", setting.getName()));
    return settingsCollection
        .find(filter)
        .map(document -> document.get("extra", Document.class).getString(key))
        .first();
  }

  @Override
  public MongoDatabaseConfiguration getConfiguration() {
    return databaseConfiguration;
  }

  @Override
  public boolean isConnected() {
    return client != null;
  }

  private MongoClient createMongoClient() {
    ConnectionString connectionURI = new ConnectionString(getConfiguration().getConnectionURI());
    return MongoClients.create(
        MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .compressorList(Collections.singletonList(MongoCompressor.createSnappyCompressor()))
            .applyConnectionString(connectionURI)
            .build());
  }

  private MongoCollection<Document> getSettingsCollection(MongoClient client) {
    return client
        .getDatabase(getConfiguration().getDatabaseName())
        .getCollection("playersettings_settings");
  }
}
