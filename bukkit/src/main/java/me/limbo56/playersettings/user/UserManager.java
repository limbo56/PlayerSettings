package me.limbo56.playersettings.user;

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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserManager implements SettingsWatchlist {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Map<UUID, SettingUser> userMap = new ConcurrentHashMap<>();

  public void loadUser(UUID uuid) {
    this.loadUsers(Collections.singleton(uuid));
  }

  public void loadUsers(Collection<UUID> uuids) {
    Collection<Setting> registeredSettings = PLUGIN.getSettingsManager().getSettingMap().values();
    for (UUID uuid : uuids) {
      PLUGIN.getLogger().config("Loading user `" + uuid + "`");
      Optional<SettingWatcher> optionalSavedSettings = getSavedSettings(uuid);
      PLUGIN.getLogger().config("Has saved settings `" + optionalSavedSettings.isPresent() + "`");

      new TaskChain()
          .sync(
              data -> {
                SettingUser user = getUser(uuid);
                SettingWatcher settingWatcher = user.getSettingWatcher();

                optionalSavedSettings.ifPresent(
                    savedSettings -> loadSavedSettings(savedSettings, settingWatcher));
                loadNewSettings(registeredSettings, settingWatcher);

                user.setLoading(false);
                userMap.put(user.getUniqueId(), user);
              })
          .runSync();
    }
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
    Collection<UUID> onlineUsersUUID =
        Bukkit.getOnlinePlayers().stream()
            .filter(
                player ->
                    PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName()))
            .map(Entity::getUniqueId)
            .collect(Collectors.toList());
    this.loadUsers(onlineUsersUUID);
  }

  public void saveUsers(Collection<SettingWatcher> settings) {
    PLUGIN.getSettingsDatabase().saveSettingWatchers(settings);
  }

  public void saveAll() {
    this.saveUsers(
        this.getUsers().stream().map(SettingUser::getSettingWatcher).collect(Collectors.toList()));
  }

  public void saveUser(UUID uuid) {
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
