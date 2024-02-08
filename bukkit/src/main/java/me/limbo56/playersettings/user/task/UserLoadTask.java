package me.limbo56.playersettings.user.task;

import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.user.action.FlightStateLoadAction;
import me.limbo56.playersettings.user.action.UserAction;
import me.limbo56.playersettings.util.Permissions;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserLoadTask implements TaskChain.Task {
  private static final List<UserAction> POST_LOAD_ACTIONS =
      Collections.singletonList(new FlightStateLoadAction());
  private final UUID uuid;
  private final UserManager userManager;
  private final SettingsManager settingsManager;
  private final PluginConfiguration pluginConfiguration;

  public UserLoadTask(UUID uuid) {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.uuid = uuid;
    this.userManager = plugin.getUserManager();
    this.settingsManager = plugin.getSettingsManager();
    this.pluginConfiguration = plugin.getConfiguration();
  }

  @Override
  public void accept(Map<String, Object> data) {
    Optional<SettingWatcher> savedSettings =
        Optional.ofNullable((SettingWatcher) data.get("settings"));
    PluginLogger.debug("Has saved settings `" + savedSettings.isPresent() + "`");

    Collection<? extends Setting> settings = settingsManager.getSettings();
    SettingUser user = userManager.getUser(uuid);
    SettingWatcher settingWatcher = user.getSettingWatcher();
    try {
      savedSettings.ifPresent(watcher -> loadSavedSettings(watcher, settingWatcher));
      loadNewSettings(settings, settingWatcher);
      loadUser(user);
    } catch (NullPointerException exception) {
      handleNullPointerException(exception);
    }

    data.put("user", user);
  }

  private void loadSavedSettings(
      SettingWatcher savedSettings, SettingWatcher targetSettingWatcher) {
    for (String settingName : savedSettings.getWatched()) {
      loadSavedSetting(settingName, savedSettings, targetSettingWatcher);
    }
  }

  private void loadSavedSetting(
      String settingName, SettingWatcher savedSettings, SettingWatcher targetSettingWatcher) {
    Setting setting = settingsManager.getSetting(settingName);
    int safeValue = getSafeValue(savedSettings, settingName);
    boolean isMissingJoinTrigger = !setting.hasTrigger("join");
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

  private void loadNewSettings(
      Collection<? extends Setting> settings, SettingWatcher settingWatcher) {
    List<String> watched = Arrays.asList(settingWatcher.getWatched());
    for (Setting setting : settings) {
      if (!watched.contains(setting.getName())) {
        loadMissingSetting(setting, settingWatcher);
      }
    }
  }

  private void loadMissingSetting(Setting setting, SettingWatcher settingWatcher) {
    List<String> watched = Arrays.asList(settingWatcher.getWatched());
    String settingName = setting.getName();
    if (watched.contains(settingName)) {
      return;
    }

    int defaultValue = setting.getDefaultValue();
    boolean isMissingJoinTrigger = !setting.hasTrigger("join");
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

  private static void loadUser(SettingUser user) {
    // Mark user as loaded
    user.setLoading(false);

    // Execute post load actions
    for (UserAction actionHandler : POST_LOAD_ACTIONS) {
      if (actionHandler.shouldExecute()) {
        actionHandler.accept(user);
      }
    }
  }

  private void handleNullPointerException(NullPointerException exception) {
    // Add a warning to prevent an exception when players disconnect
    // while their settings are loading
    if (Bukkit.getPlayer(uuid) == null) {
      if (pluginConfiguration.hasOfflineWarningEnabled() || pluginConfiguration.hasDebugEnabled()) {
        PluginLogger.warning("Failed to load settings for offline user `" + uuid + "`");
        PluginLogger.warning(
            "This warning may be caused by a security/authentication plugin! You can turn off this warning in the `config.yml` file by setting the `general.offline-warning` option to `false`.");
      }
      if (pluginConfiguration.hasDebugEnabled()) {
        PluginLogger.severe("An exception occurred while loading a null user", exception);
      }
      return;
    }

    PluginLogger.severe("An exception occurred while loading user '" + uuid + "'", exception);
  }

  private int getSafeValue(SettingWatcher settingWatcher, String settingName) {
    Setting setting = settingsManager.getSetting(settingName);
    Player player = Bukkit.getPlayer(settingWatcher.getOwner());
    int value = settingWatcher.getValue(settingName);
    int maxValue = Permissions.getSettingPermissionLevel(player, setting);
    return Math.abs(value) > maxValue ? setting.getDefaultValue() : value;
  }
}
