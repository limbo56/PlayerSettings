package me.limbo56.playersettings.user;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.api.registry.SettingsWatchlist;
import me.limbo56.playersettings.user.task.UnloadUserTask;
import me.limbo56.playersettings.user.task.UserLoadTask;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserManager implements SettingsWatchlist {
  private final PlayerSettings plugin;
  private final Map<UUID, SettingUser> userMap = new ConcurrentHashMap<>();

  public UserManager(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  public void saveUsers(Collection<SettingWatcher> settingWatchers) {
    plugin.getDataManager().saveSettingWatchers(settingWatchers);
  }

  public void saveUsers() {
    PluginLogger.info("Saving all player settings...");
    this.saveUsers(
        this.getUsers().stream().map(SettingUser::getSettingWatcher).collect(Collectors.toList()));
  }

  public void saveUser(UUID uuid) {
    PluginLogger.info("Saving settings of player '" + uuid + "'");
    this.saveUsers(Collections.singleton(this.getSettingWatcher(uuid)));
  }

  public void loadUser(UUID uuid) {
    this.loadUsers(Collections.singleton(uuid));
  }

  public void loadUsers(Collection<UUID> uuids) {
    for (UUID uuid : uuids) {
      PluginLogger.info("Loading settings of player '" + uuid + "'");
      new TaskChain()
          .loadAsync("settings", () -> plugin.getDataManager().loadSettingWatcher(uuid))
          .sync(new UserLoadTask(uuid))
          .sync(map -> userMap.put(uuid, (SettingUser) map.get("user")))
          .runAsync(plugin);
    }
  }

  public void loadOnlineUsers() {
    Collection<UUID> online = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (plugin.getConfiguration().isAllowedWorld(player.getWorld().getName())) {
        online.add(player.getUniqueId());
      }
    }
    this.loadUsers(online);
  }

  public void unloadUser(UUID uuid) {
    getUser(uuid).clearSettingEffects();
    new TaskChain()
        .async(new UnloadUserTask(uuid))
        .sync(map -> userMap.remove(uuid))
        .runAsync(plugin);
  }

  public void unloadAll() {
    userMap.clear();
  }

  public boolean isUserLoaded(UUID uuid) {
    return userMap.containsKey(uuid);
  }

  public SettingUser getUser(UUID uuid) {
    return userMap.getOrDefault(uuid, new SettingUser(uuid));
  }

  public Collection<SettingUser> getUsers() {
    return userMap.values();
  }

  public Collection<SettingUser> getUsersBySetting(String settingName, boolean enabled) {
    return getUsers().stream()
        .filter(user -> user.hasSettingEnabled(settingName) == enabled)
        .collect(Collectors.toList());
  }

  @Override
  public SettingWatcher getSettingWatcher(UUID uuid) {
    return getUser(uuid).getSettingWatcher();
  }
}
