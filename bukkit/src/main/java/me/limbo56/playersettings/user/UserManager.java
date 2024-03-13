package me.limbo56.playersettings.user;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.api.registry.SettingsWatchlist;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.database.DataManager;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.action.FlightStateLoadAction;
import me.limbo56.playersettings.user.task.UnloadUserTask;
import me.limbo56.playersettings.user.task.UserLoadTask;
import me.limbo56.playersettings.util.Debouncer;
import me.limbo56.playersettings.util.MemoDebouncer;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserManager implements SettingsWatchlist {
  private final PlayerSettings plugin;
  private final DataManager dataManager;
  private final SettingsManager settingsManager;
  private final PluginConfiguration pluginConfiguration;
  private final Map<UUID, SettingUser> userMap = new ConcurrentHashMap<>();
  private Debouncer<UUID> saveUserDebounced;
  private Debouncer<UUID> saveFlightStateDebounced;

  public UserManager(PlayerSettings plugin) {
    this.plugin = plugin;
    this.dataManager = plugin.getDataManager();
    this.settingsManager = plugin.getSettingsManager();
    this.pluginConfiguration = plugin.getConfiguration();
  }

  public void saveUsers(Collection<SettingWatcher> settingWatchers) {
    dataManager.saveSettingWatchers(settingWatchers);
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
          .loadAsync("settings", () -> dataManager.loadSettingWatcher(uuid))
          .sync(new UserLoadTask(uuid))
          .sync(map -> userMap.put(uuid, (SettingUser) map.get("user")))
          .runAsync(plugin);
    }
  }

  public void loadOnlineUsers() {
    Collection<UUID> online = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
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
    if (saveUserDebounced != null) {
      saveUserDebounced.terminate();
      saveUserDebounced = null;
    }
    if (saveFlightStateDebounced != null) {
      saveFlightStateDebounced.terminate();
      saveFlightStateDebounced = null;
    }
    userMap.clear();
  }

  public boolean isUserLoaded(UUID uuid) {
    return userMap.containsKey(uuid);
  }

  @Override
  public SettingWatcher getSettingWatcher(UUID uuid) {
    return getUser(uuid).getSettingWatcher();
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

  public Debouncer<UUID> getSaveUserDebounced() {
    if (this.saveUserDebounced == null) {
      this.saveUserDebounced =
          new Debouncer<>(this::saveUser, pluginConfiguration.getSettingsSaveDelay());
    }
    return this.saveUserDebounced;
  }

  public Debouncer<UUID> getSaveFlightStateDebounced() {
    if (this.saveFlightStateDebounced == null) {
      this.saveFlightStateDebounced =
          new MemoDebouncer<>(this::getFlightState, this::saveFlightState, pluginConfiguration.getFlightStateSaveDelay());
    }
    return this.saveFlightStateDebounced;
  }

  private int getFlightState(UUID uuid) {
    return getUser(uuid).isFlying() ? 1 : 0;
  }

  private void saveFlightState(UUID uuid) {
    try {
      Setting setting = settingsManager.getSetting(Settings.fly().getName());
      String value = String.valueOf(getFlightState(uuid));
      PluginLogger.debug("Saving flight state '" + value + "' of player '" + uuid + "'");
      dataManager.putExtra(uuid, setting, FlightStateLoadAction.STATE_KEY, value);
    } catch (Exception e) {
      PluginLogger.severe(
          "An exception occurred while loading flight state of player '" + uuid + "'", e);
    }
  }
}
