package me.limbo56.playersettings.settings;

import com.google.common.base.Preconditions;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
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
    String settingName =
        Preconditions.checkNotNull(setting.getName(), "Setting name cannot be null");
    Preconditions.checkArgument(
        !settingMap.containsKey(settingName),
        "Error registering setting '"
            + settingName
            + "'. A setting with the same name is already registered!");

    Setting configuredSetting = PLUGIN.getSettingsConfiguration().parseSetting(setting);
    if (configuredSetting == null || !configuredSetting.isEnabled()) {
      PluginLogger.debug(
          "Skipping registration of setting '"
              + settingName
              + "'. Missing or not enabled in configuration.");
      return;
    }

    // Register setting
    settingMap.putIfAbsent(settingName, configuredSetting);
    if (isCustomSetting) {
      customSettings.putIfAbsent(settingName, setting);
    }

    // Register setting listeners
    for (Listener listener : configuredSetting.getListeners()) {
      PLUGIN.getListenerManager().registerListener(listener);
    }

    PluginLogger.debug("Registered setting '" + settingName + "'");
  }

  @Override
  public void unregisterSetting(String settingName) {
    settingMap.computeIfPresent(
        settingName,
        (name, setting) -> {
          setting.getListeners().forEach(PLUGIN.getListenerManager()::unregisterListener);
          return null;
        });
  }

  public void reloadSettings() {
    List<String> loadList = new ArrayList<>(settingMap.keySet());
    SettingsConfiguration settingsConfiguration = PLUGIN.getSettingsConfiguration();

    // Unregister loaded settings
    loadList.forEach(this::unregisterSetting);

    // Remove disabled/missing settings from the load list
    loadList.removeIf(
        settingName -> {
          if (settingsConfiguration.isSettingEnabled(settingName)) {
            return false;
          }
          PluginLogger.log("Removed setting '" + settingName + "'");
          return true;
        });

    // Add new settings to the load list
    for (Setting setting1 : settingsConfiguration.getEnabledSettings(true)) {
      String name = setting1.getName();
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
    return Arrays.stream(setting.getTriggers()).anyMatch(Arrays.asList(triggers)::contains);
  }

  @NotNull
  public Collection<String> getAllowedSettings(UUID uuid) {
    List<String> allowedSettings = new ArrayList<>();
    for (Setting setting : settingMap.values()) {
      String settingName = setting.getName();
      if (PLUGIN.getUserManager().getUser(uuid).hasSettingPermissions(settingName))
        allowedSettings.add(settingName);
    }
    return allowedSettings;
  }

  @Override
  public Setting getSetting(String settingName) {
    return settingMap.get(settingName);
  }

  public Collection<String> getSettingNames() {
    List<String> settingNames = new ArrayList<>();
    for (Setting setting : settingMap.values()) {
      String name = setting.getName();
      settingNames.add(name);
    }
    return settingNames;
  }

  public Map<String, Setting> getSettingMap() {
    return settingMap;
  }
}
