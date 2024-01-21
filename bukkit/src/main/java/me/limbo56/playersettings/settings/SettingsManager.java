package me.limbo56.playersettings.settings;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.listeners.ListenerManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class SettingsManager implements SettingsContainer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final ConcurrentMap<String, Setting> settingMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Setting> customSettings = new ConcurrentHashMap<>();

  @Override
  public void registerSetting(Setting setting) {
    registerSetting(setting, true);
  }

  public void registerSetting(Setting setting, boolean isCustomSetting) {
    String settingName = setting.getName();
    if (settingName == null) {
      PluginLogger.severe("Setting name cannot be null");
      return;
    }
    if (settingMap.containsKey(settingName)) {
      PluginLogger.severe(
          "Error registering setting '"
              + settingName
              + "'. A setting with the same name is already registered!");
      return;
    }

    Setting configuredSetting = PLUGIN.getSettingsConfiguration().parseSetting(setting);
    if (configuredSetting == null || !configuredSetting.isEnabled()) {
      PluginLogger.log(
          "Skipping registration of setting '"
              + settingName
              + "'. Missing or not enabled in configuration.");
      return;
    }

    // Register setting
    if (isCustomSetting) {
      customSettings.put(settingName, setting);
    }
    settingMap.put(settingName, configuredSetting);

    // Register setting listeners
    for (Listener listener : configuredSetting.getListeners()) {
      PLUGIN.getListenerManager().registerListener(listener);
    }

    PluginLogger.debug("Registered setting '" + settingName + "'");
  }

  @Override
  public void unregisterSetting(String settingName) {
    Setting setting = settingMap.get(settingName);
    if (setting == null) {
      return;
    }

    ListenerManager listenerManager = PLUGIN.getListenerManager();
    for (Listener listener : setting.getListeners()) {
      listenerManager.unregisterListener(listener);
    }
    settingMap.remove(settingName);
  }

  public void reloadSettings() {
    SettingsConfiguration settingsConfiguration = PLUGIN.getSettingsConfiguration();
    Set<String> loadList = getSettingNames();
    Set<String> settingsToRemove = new HashSet<>();

    for (String settingName : loadList) {
      // Unregister loaded settings
      unregisterSetting(settingName);

      // Remove disabled/missing settings from the load list
      if (!settingsConfiguration.isSettingEnabled(settingName)) {
        PluginLogger.log("Removed setting '" + settingName + "'");
        settingsToRemove.add(settingName);
      }
    }

    // Remove disabled/missing settings from the load list
    loadList.removeAll(settingsToRemove);

    // Add new settings to the load list
    for (Setting setting : settingsConfiguration.getEnabledSettings(true)) {
      String name = setting.getName();
      if (!loadList.contains(name)) {
        PluginLogger.log("New setting '" + name + "'");
        loadList.add(name);
      }
    }

    // Load the new settings
    for (String settingName : loadList) {
      boolean customSetting = isCustomSetting(settingName);
      Setting setting =
          customSetting
              ? settingsConfiguration.mergeSettingWithConfiguration(customSettings.get(settingName))
              : settingsConfiguration.parseSetting(settingName);
      registerSetting(setting, customSetting);
    }
  }

  public void unloadAll() {
    settingMap.clear();
    customSettings.clear();
  }

  public boolean isCustomSetting(String settingName) {
    return customSettings.containsKey(settingName);
  }

  public boolean isSettingRegistered(String settingName) {
    return settingMap.containsKey(settingName);
  }

  public boolean hasTriggers(Setting setting, String... triggers) {
    List<String> strings = Arrays.asList(triggers);
    for (String s : setting.getTriggers()) {
      if (strings.contains(s)) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  public Collection<String> getAllowedSettings(UUID uuid) {
    List<String> allowedSettings = new ArrayList<>();
    SettingUser user = PLUGIN.getUserManager().getUser(uuid);
    for (String settingName : settingMap.keySet()) {
      if (user.hasSettingPermissions(settingName)) {
        allowedSettings.add(settingName);
      }
    }
    return allowedSettings;
  }

  @Override
  public Setting getSetting(String settingName) {
    return settingMap.get(settingName);
  }

  public Set<String> getSettingNames() {
    return new HashSet<>(settingMap.keySet());
  }

  public Collection<Setting> getSettings() {
    return settingMap.values();
  }

  public Map<String, Setting> getSettingMap() {
    return settingMap;
  }
}
