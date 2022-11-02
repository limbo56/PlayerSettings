package me.limbo56.playersettings.settings;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultSettingsContainer implements SettingsContainer {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private final ConcurrentMap<String, Setting> settingMap;

  public DefaultSettingsContainer() {
    this.settingMap = new ConcurrentHashMap<>();
  }

  @Override
  public void registerSetting(Setting setting) {
    String settingName =
        Preconditions.checkNotNull(setting.getName(), "Setting name cannot be null");
    this.configureSetting(setting);

    if (settingMap.containsKey(settingName)) {
      String message =
          "Skipping registration of setting '"
              + settingName
              + "'. A setting with the same name is already registered!";
      plugin.getLogger().config(message);
      return;
    }

    Setting configuredSetting = PlayerSettingsProvider.getConfiguredSetting(setting);
    if (configuredSetting == null || !configuredSetting.isEnabled()) {
      String message =
          "Skipping registration of setting '"
              + settingName
              + "'. Missing or not enabled in configuration.";
      plugin.getLogger().config(message);
      return;
    }

    this.loadSetting(configuredSetting);
  }

  private void configureSetting(Setting setting) {
    String name = setting.getName();
    if (PlayerSettingsProvider.isSettingConfigured(name)) {
      return;
    }

    if (!plugin.getSettingsConfiguration().hasSetting(name)) {
      plugin.getSettingsConfiguration().configureSetting(setting);
    }

    YamlConfiguration itemsConfiguration = plugin.getItemsConfiguration();
    ConfigUtil.configureSerializable(itemsConfiguration, name, setting.getItem());
    try {
      File pluginFile = ConfigUtil.getPluginFile("items.yml");
      itemsConfiguration.save(pluginFile);
    } catch (IOException e) {
      plugin.getLogger().severe("Failed to save setting '" + name + "' to configuration");
      e.printStackTrace();
    }
  }

  public void reloadSettings() {
    // Unload settings
    Collection<Setting> settings = new ArrayList<>(settingMap.values());
    Collection<String> settingNames =
        settings.stream().map(Setting::getName).collect(Collectors.toList());
    settingNames.forEach(this::unloadSetting);

    // Log removed settings
    Collection<String> removedSettings =
        settingNames.stream()
            .filter(setting -> !PlayerSettingsProvider.isSettingConfigured(setting))
            .collect(Collectors.toList());
    removedSettings.forEach(
        removedSetting -> plugin.getLogger().info("Removed setting '" + removedSetting + "'"));
    settingNames.removeAll(removedSettings);

    // Log disabled settings
    Collection<String> disabledSettings =
        settingNames.stream()
            .filter(setting -> !PlayerSettingsProvider.getSettingByName(setting).isEnabled())
            .collect(Collectors.toList());
    disabledSettings.forEach(
        disabledSetting -> plugin.getLogger().info("Disabled setting '" + disabledSetting + "'"));
    settingNames.removeAll(disabledSettings);

    // Log new settings
    Collection<Setting> newSettings =
        plugin.getSettingsConfiguration().getSettingsFromConfiguration().stream()
            .filter(setting -> !settingNames.contains(setting.getName()))
            .collect(Collectors.toList());
    newSettings.forEach(
        newSetting ->
            plugin.getLogger().info("Detected new setting '" + newSetting.getName() + "'"));
    settings.addAll(newSettings);

    // Load settings
    settings.stream()
        .filter(setting -> settingNames.contains(setting.getName()))
        .map(PlayerSettingsProvider::getConfiguredSetting)
        .forEach(
            setting -> {
              loadSetting(setting);
              plugin.getLogger().config("Registered setting '" + setting.getName() + "'");
            });
  }

  public void loadSetting(Setting setting) {
    String settingName = setting.getName();
    setting.getListeners().forEach(plugin.getListenerManager()::registerListener);
    settingMap.putIfAbsent(settingName, setting);
  }

  private void unloadSetting(String settingName) {
    Optional.ofNullable(settingMap.remove(settingName))
        .map(Setting::getListeners)
        .ifPresent(listeners -> listeners.forEach(plugin.getListenerManager()::unregisterListener));
  }

  public boolean isSettingLoaded(String settingName) {
    return settingMap.containsKey(settingName);
  }

  public void unloadAll() {
    settingMap.clear();
  }

  @Override
  public Setting getSetting(String settingName) {
    return settingMap.get(settingName);
  }

  public Map<String, Setting> getSettingMap() {
    return settingMap;
  }
}
