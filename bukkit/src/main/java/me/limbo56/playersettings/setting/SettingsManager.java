package me.limbo56.playersettings.setting;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.registry.SettingsContainer;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;

/** Manages settings for the plugin. */
public class SettingsManager implements SettingsContainer {
  private final ConcurrentMap<String, InternalSetting> settingMap = new ConcurrentHashMap<>();
  private final SettingsConfiguration settingsConfiguration;

  public SettingsManager(PlayerSettings plugin) {
    this.settingsConfiguration =
        plugin.getConfigurationManager().getConfiguration(SettingsConfiguration.class);
  }

  public void registerDefaultSettings() {
    // Register default settings
    Settings.register();
    for (Setting setting : Settings.getDefaultSettings()) {
      register(setting);
    }

    // Register enabled settings
    for (Setting setting : settingsConfiguration.getEnabledSettings(false)) {
      if (!isRegistered(setting.getName())) {
        registerSetting(setting, false);
      }
    }
  }

  @Override
  public void register(Setting setting) {
    registerSetting(setting, true);
  }

  private void registerSetting(Setting setting, boolean custom) {
    String settingName = setting.getName();
    if (settingMap.containsKey(settingName)) {
      PluginLogger.severe(
          "Error registering setting '"
              + settingName
              + "'. A setting with the same name is already registered!");
      return;
    }

    InternalSetting internalSetting = InternalSetting.from(setting, custom);
    if (!internalSetting.isEnabled()) {
      PluginLogger.info(
          "Skipping registration of setting '"
              + settingName
              + "'. Missing or not enabled in configuration.");
      return;
    }

    internalSetting.register();
    settingMap.put(settingName, internalSetting);
    PluginLogger.debug("Registered setting '" + settingName + "'");
  }

  @Override
  public void unregister(String settingName) {
    InternalSetting setting = settingMap.get(settingName);
    if (setting != null) {
      setting.unregister();
      settingMap.remove(settingName);
    }
  }

  public void unloadAll() {
    settingMap.clear();
  }

  public void reloadSettings() {
    Set<String> loadList = getSettingNames();
    Set<String> settingsToRemove = new HashSet<>();
    Map<String, InternalSetting> customSettings =
        getSettings().stream()
            .filter(InternalSetting::isCustom)
            .collect(Collectors.toMap(Setting::getName, Function.identity()));

    for (String settingName : loadList) {
      unregister(settingName);

      // Remove disabled/missing settings from the load list
      if (!settingsConfiguration.isSettingEnabled(settingName)) {
        PluginLogger.info("Removed setting '" + settingName + "'");
        settingsToRemove.add(settingName);
      }
    }

    // Remove disabled/missing settings from the load list
    loadList.removeAll(settingsToRemove);

    // Add new settings to the load list
    for (Setting setting : settingsConfiguration.getEnabledSettings(true)) {
      String name = setting.getName();
      if (!loadList.contains(name)) {
        PluginLogger.info("New setting '" + name + "'");
        loadList.add(name);
      }
    }

    // Load the new settings
    for (String settingName : loadList) {
      InternalSetting setting;
      if (customSettings.containsKey(settingName)) {
        setting = InternalSetting.fromInternal(customSettings.get(settingName));
      } else {
        ConfigurationSection section =
            settingsConfiguration.getFile().getConfigurationSection(settingName);
        setting = InternalSetting.from(Parsers.SETTING_PARSER.parse(section));
      }
      registerSetting(setting, setting.isCustom());
    }
  }

  public boolean isRegistered(String settingName) {
    return settingMap.containsKey(settingName);
  }

  @Override
  public <T extends Setting> T getSetting(String settingName) {
    return (T) settingMap.get(settingName);
  }

  @Override
  public Collection<InternalSetting> getSettings() {
    return settingMap.values();
  }

  public Set<String> getSettingNames() {
    return new HashSet<>(settingMap.keySet());
  }
}
