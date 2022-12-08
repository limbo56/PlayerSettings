package me.limbo56.playersettings.user;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsWatchlist;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserManager implements SettingsWatchlist {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Map<UUID, SettingUser> userCache = new ConcurrentHashMap<>();

  public void loadUsers(Collection<UUID> uuids) {
    for (SettingWatcher watcher : PLUGIN.getSettingsDatabase().loadUserSettings(uuids)) {
      SettingUser user = getUser(watcher.getOwner());
      user.setSettingWatcher(watcher);
      user.setLoading(false);
      userCache.put(user.getUniqueId(), user);
    }
  }

  public void loadUser(UUID uuid) {
    this.loadUsers(Collections.singleton(uuid));
  }

  public void loadOnlineUsers() {
    Collection<UUID> onlineUsersUUID =
        Bukkit.getOnlinePlayers().stream()
            .filter(player -> PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName()))
            .map(Entity::getUniqueId)
            .collect(Collectors.toList());
    this.loadUsers(onlineUsersUUID);
  }

  public void saveUsers(Collection<SettingWatcher> settings) {
    PLUGIN.getSettingsDatabase().saveUserSettings(settings);
  }

  public void saveAll() {
    this.saveUsers(
        this.getUsers().stream().map(SettingUser::getSettingWatcher).collect(Collectors.toList()));
  }

  public void saveUser(UUID uuid) {
    this.saveUsers(Collections.singleton(this.getUser(uuid).getSettingWatcher()));
  }

  public void unloadUser(UUID uuid) {
    userCache.remove(uuid);
  }

  public void unloadAll() {
    userCache.clear();
  }

  public boolean isUserLoaded(UUID uuid) {
    return userCache.containsKey(uuid);
  }

  @Override
  public SettingWatcher getSettingWatcher(UUID uuid) {
    return this.getUser(uuid).getSettingWatcher();
  }

  public SettingUser getUser(UUID uuid) {
    return userCache.getOrDefault(uuid, new SettingUser(uuid));
  }

  public Collection<SettingUser> getUsers() {
    return userCache.values();
  }

  public Collection<SettingUser> getUsersWithSettingValue(String settingName, boolean enabled) {
    return getUsers().stream()
        .filter(user -> user.hasSettingEnabled(settingName) == enabled)
        .collect(Collectors.toList());
  }
}
