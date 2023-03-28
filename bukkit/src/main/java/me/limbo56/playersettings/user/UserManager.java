package me.limbo56.playersettings.user;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsWatchlist;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.util.Permissions;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserManager implements SettingsWatchlist {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Map<UUID, SettingUser> userMap = new ConcurrentHashMap<>();

  public void loadUser(UUID uuid) {
    this.loadUsers(Collections.singleton(uuid));
  }

  public void loadUsers(Collection<UUID> uuids) {
    Collection<Setting> registeredSettings = PLUGIN.getSettingsManager().getSettingMap().values();
    for (UUID uuid : uuids) {
      PLUGIN.getLogger().fine("Loading settings of player '" + uuid + "'");
      new TaskChain().sync(createLoadTask(uuid, registeredSettings)).runSync();
    }
  }

  @NotNull
  private Consumer<Map<String, Object>> createLoadTask(
      UUID uuid, Collection<Setting> registeredSettings) {
    Optional<SettingWatcher> optionalSavedSettings = getSavedSettings(uuid);
    PLUGIN.getLogger().config("Has saved settings `" + optionalSavedSettings.isPresent() + "`");

    return data -> {
      // Load saved and new settings
      SettingUser user = getUser(uuid);
      SettingWatcher settingWatcher = user.getSettingWatcher();
      try {
        optionalSavedSettings.ifPresent(watcher -> loadSavedSettings(watcher, settingWatcher));
        loadNewSettings(registeredSettings, settingWatcher);
      } catch (NullPointerException exception) {
        // Add warning to prevent exception when player disconnects and their settings are loading
        if (Bukkit.getPlayer(uuid) == null) {
          if (PLUGIN.getPluginConfiguration().hasOfflineWarningEnabled()
              || PLUGIN.getPluginConfiguration().hasDebugEnabled()) {
            PLUGIN.getLogger().warning("Failed to load settings for offline user `" + uuid + "`");
            PLUGIN
                .getLogger()
                .warning(
                    "This warning may be caused by a security/authentication plugin! You can turn off this warning in the `config.yml` file by setting the `general.offline-warning` option to `false`.");
          }
          if (PLUGIN.getPluginConfiguration().hasDebugEnabled()) {
            exception.printStackTrace();
          }
          return;
        }

        exception.printStackTrace();
      }

      user.setLoading(false);
      userMap.put(user.getUniqueId(), user);
    };
  }

  private void loadNewSettings(
      Collection<Setting> registeredSettings, SettingWatcher settingWatcher) {
    for (Setting setting : registeredSettings) {
      String settingName = setting.getName();
      if (Arrays.asList(settingWatcher.getWatched()).contains(settingName)) {
        continue;
      }

      int defaultValue = setting.getDefaultValue();
      boolean isMissingJoinTrigger = !PLUGIN.getSettingsManager().hasTriggers(setting, "join");
      settingWatcher.setValue(settingName, defaultValue, isMissingJoinTrigger);
      PLUGIN
          .getLogger()
          .config(
              String.format(
                  "Loaded new setting `%s` with value `%d` silent `%s`",
                  settingName, defaultValue, isMissingJoinTrigger));
    }
  }

  private void loadSavedSettings(
      SettingWatcher savedSettings, SettingWatcher targetSettingWatcher) {
    for (String settingName : savedSettings.getWatched()) {
      Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
      int safeValue = getSafeValue(savedSettings, settingName);
      boolean isMissingJoinTrigger = !PLUGIN.getSettingsManager().hasTriggers(setting, "join");
      targetSettingWatcher.setValue(settingName, safeValue, isMissingJoinTrigger);
      PLUGIN
          .getLogger()
          .config(
              String.format(
                  "Loaded saved setting `%s` with value `%d` silent `%s`",
                  settingName, safeValue, isMissingJoinTrigger));
    }
  }

  private int getSafeValue(SettingWatcher settingWatcher, String settingName) {
    Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
    Player player = Bukkit.getPlayer(settingWatcher.getOwner());
    int value = settingWatcher.getValue(settingName);
    int maxValue = Permissions.getSettingPermissionLevel(player, setting);
    return Math.abs(value) > maxValue ? setting.getDefaultValue() : value;
  }

  public void loadOnlineUsers() {
    Collection<UUID> onlineUsers =
        Bukkit.getOnlinePlayers().stream()
            .filter(
                player ->
                    PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName()))
            .map(Entity::getUniqueId)
            .collect(Collectors.toList());
    this.loadUsers(onlineUsers);
  }

  public void saveUsers(Collection<SettingWatcher> settings) {
    PLUGIN.getSettingsDatabase().saveSettingWatchers(settings);
  }

  public void saveAll() {
    this.saveUsers(
        this.getUsers().stream().map(SettingUser::getSettingWatcher).collect(Collectors.toList()));
  }

  public void saveUser(UUID uuid) {
    PLUGIN.getLogger().fine("Saving settings of player '" + uuid + "'");
    this.saveUsers(Collections.singleton(this.getUser(uuid).getSettingWatcher()));
  }

  public void unloadUser(UUID uuid) {
    userMap.remove(uuid);
  }

  public void unloadAll() {
    userMap.clear();
  }

  public boolean isUserLoaded(UUID uuid) {
    return userMap.containsKey(uuid);
  }

  @Override
  public SettingWatcher getSettingWatcher(UUID uuid) {
    return this.getUser(uuid).getSettingWatcher();
  }

  @NotNull
  public Optional<SettingWatcher> getSavedSettings(UUID uuid) {
    return PLUGIN
        .getSettingsDatabase()
        .loadSettingWatchers(Collections.singletonList(uuid))
        .stream()
        .findFirst();
  }

  public Collection<SettingUser> getUsersWithSettingValue(String settingName, boolean enabled) {
    return getUsers().stream()
        .filter(user -> user.hasSettingEnabled(settingName) == enabled)
        .collect(Collectors.toList());
  }

  public SettingUser getUser(UUID uuid) {
    return userMap.getOrDefault(uuid, new SettingUser(uuid));
  }

  public Collection<SettingUser> getUsers() {
    return userMap.values();
  }
}
