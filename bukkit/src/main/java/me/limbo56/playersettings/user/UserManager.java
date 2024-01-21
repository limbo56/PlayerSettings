package me.limbo56.playersettings.user;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsWatchlist;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.util.Permissions;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserManager implements SettingsWatchlist {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Map<UUID, SettingUser> userMap = new ConcurrentHashMap<>();

  public void loadUser(UUID uuid) {
    this.loadUsers(Collections.singleton(uuid));
  }

  public void loadUsers(Collection<UUID> uuids) {
    for (UUID uuid : uuids) {
      PluginLogger.log("Loading settings of player '" + uuid + "'");
      new TaskChain()
          .sync(createLoadTask(uuid, PLUGIN.getSettingsManager().getSettings()))
          .runSync();
    }
  }

  @NotNull
  private Consumer<Map<String, Object>> createLoadTask(
      UUID uuid, Collection<Setting> registeredSettings) {
    Optional<SettingWatcher> optionalSavedSettings = getSavedSettings(uuid);
    PluginLogger.debug("Has saved settings `" + optionalSavedSettings.isPresent() + "`");

    return data -> {
      // Load saved and new settings
      SettingUser user = getUser(uuid);
      SettingWatcher settingWatcher = user.getSettingWatcher();
      try {
        optionalSavedSettings.ifPresent(watcher -> loadSavedSettings(watcher, settingWatcher));
        loadNewSettings(registeredSettings, settingWatcher);
      } catch (NullPointerException exception) {
        // Add a warning to prevent an exception when players disconnect
        // while their settings are loading
        if (Bukkit.getPlayer(uuid) == null) {
          if (PLUGIN.getPluginConfiguration().hasOfflineWarningEnabled()
              || PLUGIN.getPluginConfiguration().hasDebugEnabled()) {
            PluginLogger.warning("Failed to load settings for offline user `" + uuid + "`");
            PluginLogger.warning(
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
      PluginLogger.debug(
          "Loaded new setting "
              + "`"
              + settingName
              + "` "
              + "with value "
              + "`"
              + defaultValue
              + "` "
              + "silent "
              + isMissingJoinTrigger);
    }
  }

  private void loadSavedSettings(
      SettingWatcher savedSettings, SettingWatcher targetSettingWatcher) {
    for (String settingName : savedSettings.getWatched()) {
      Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
      int safeValue = getSafeValue(savedSettings, settingName);
      boolean isMissingJoinTrigger = !PLUGIN.getSettingsManager().hasTriggers(setting, "join");
      targetSettingWatcher.setValue(settingName, safeValue, isMissingJoinTrigger);
      PluginLogger.debug(
          "Loaded saved setting "
              + "`"
              + settingName
              + "` "
              + "with value "
              + "`"
              + safeValue
              + "` "
              + "silent "
              + isMissingJoinTrigger);
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
    Collection<UUID> onlineUsers = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
        UUID uniqueId = player.getUniqueId();
        onlineUsers.add(uniqueId);
      }
    }

    this.loadUsers(onlineUsers);
  }

  public void saveUsers(Collection<SettingWatcher> settingWatchers) {
    PLUGIN.getSettingsDatabase().saveSettingWatchers(settingWatchers);
  }

  public void saveAll() {
    List<SettingWatcher> settingWatchers = new ArrayList<>();
    for (SettingUser settingUser : this.getUsers()) {
      settingWatchers.add(settingUser.getSettingWatcher());
    }

    this.saveUsers(settingWatchers);
  }

  public void saveUser(UUID uuid) {
    PluginLogger.log("Saving settings of player '" + uuid + "'");
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
    for (SettingWatcher settingWatcher :
        PLUGIN.getSettingsDatabase().loadSettingWatchers(Collections.singletonList(uuid))) {
      return Optional.of(settingWatcher);
    }
    return Optional.empty();
  }

  public Collection<SettingUser> getUsersWithSettingValue(String settingName, boolean enabled) {
    List<SettingUser> users = new ArrayList<>();
    for (SettingUser user : getUsers()) {
      if (user.hasSettingEnabled(settingName) == enabled) {
        users.add(user);
      }
    }
    return users;
  }

  public SettingUser getUser(UUID uuid) {
    return userMap.getOrDefault(uuid, new SettingUser(uuid));
  }

  public Collection<SettingUser> getUsers() {
    return userMap.values();
  }
}
