package me.limbo56.playersettings.settings;

import com.google.common.base.Preconditions;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class SettingsManager implements SettingsContainer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final ConcurrentMap<String, Setting> settingMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Setting> customSettings = new ConcurrentHashMap<>();

  @Override
  public void registerSetting(Setting setting) {
    this.registerSetting(setting, true);
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
      PLUGIN
          .getLogger()
          .config(
              "Skipping registration of setting '"
                  + settingName
                  + "'. Missing or not enabled in configuration.");
      return;
    }

    configuredSetting.getListeners().forEach(PLUGIN.getListenerManager()::registerListener);
    settingMap.putIfAbsent(settingName, configuredSetting);
    if (isCustomSetting) customSettings.put(settingName, setting);
    PLUGIN.getLogger().config("Registered setting '" + settingName + "'");
  }

  @Override
  public void unregisterSetting(String settingName) {
    Optional.ofNullable(settingMap.remove(settingName))
        .map(Setting::getListeners)
        .ifPresent(listeners -> listeners.forEach(PLUGIN.getListenerManager()::unregisterListener));
  }

  public void reloadSettings() {
    // Unregister loaded settings
    ArrayList<String> settingsToLoad = new ArrayList<>(settingMap.keySet());
    settingsToLoad.forEach(this::unregisterSetting);

    // Remove disabled/missing settings and load new settings
    SettingsConfiguration settingsConfiguration = PLUGIN.getSettingsConfiguration();
    Collection<String> enabledSettingsName =
        settingsConfiguration.getEnabledSettings(true).stream()
            .map(Setting::getName)
            .collect(Collectors.toList());
    settingsToLoad.removeIf(
        settingName -> {
          if (enabledSettingsName.contains(settingName)) return false;
          PLUGIN.getLogger().info("Removed setting '" + settingName + "'");
          return true;
        });
    enabledSettingsName.forEach(
        settingName -> {
          if (settingsToLoad.contains(settingName)) return;
          PLUGIN.getLogger().info("New setting '" + settingName + "'");
          settingsToLoad.add(settingName);
        });

    // Register the settings again
    for (String settingName : settingsToLoad) {
      Setting setting =
          isCustomSetting(settingName)
              ? settingsConfiguration.mergeSettingWithConfiguration(customSettings.get(settingName))
              : settingsConfiguration.parseSetting(settingName);
      this.registerSetting(setting, isCustomSetting(settingName));
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
    return getSettingMap().keySet().stream()
        .filter(PLUGIN.getUserManager().getUser(uuid)::hasSettingPermissions)
        .collect(Collectors.toList());
  }

  @Override
  public Setting getSetting(String settingName) {
    return settingMap.get(settingName);
  }

  public Collection<String> getSettingNames() {
    return getSettingMap().values().stream().map(Setting::getName).collect(Collectors.toList());
  }

  public Map<String, Setting> getSettingMap() {
    return settingMap;
  }
}
