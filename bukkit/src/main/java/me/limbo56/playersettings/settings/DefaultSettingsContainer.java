package me.limbo56.playersettings.settings;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
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
    if (!PlayerSettingsProvider.isSettingConfigured(settingName)) {
      if (!plugin.getSettingsConfiguration().hasSetting(settingName)) {
        plugin.getSettingsConfiguration().configureSetting(setting);
      }

      YamlConfiguration itemsConfiguration = plugin.getItemsConfiguration();
      ConfigUtil.configureSerializable(itemsConfiguration, settingName, setting.getItem());
      try {
        File pluginFile = ConfigUtil.getPluginFile("items.yml");
        itemsConfiguration.save(pluginFile);
      } catch (IOException e) {
        plugin.getLogger().severe("Failed to save setting '" + settingName + "' to configuration");
        e.printStackTrace();
      }
    }

    if (settingMap.containsKey(settingName)) {
      plugin
          .getLogger()
          .config(
              "Skipping registration of setting '"
                  + settingName
                  + "'. A setting with the same name is already registered!");
      return;
    }

    Setting configuredSetting = plugin.getSettingsConfiguration().getSetting(setting);
    if (configuredSetting == null || !configuredSetting.isEnabled()) {
      plugin
          .getLogger()
          .config(
              "Skipping registration of setting '"
                  + settingName
                  + "'. Missing or not enabled in configuration.");
      return;
    }

    this.loadSetting(configuredSetting);
  }

  public void reloadLoadedSettings() {
    // Unload settings
    Collection<String> settings =
        settingMap.values().stream().map(Setting::getName).collect(Collectors.toList());
    settings.forEach(this::unloadSetting);

    // Log removed settings
    Collection<String> removedSettings =
        settings.stream()
            .filter(setting -> !PlayerSettingsProvider.isSettingConfigured(setting))
            .collect(Collectors.toList());
    removedSettings.forEach(
        removedSetting -> plugin.getLogger().info("Removed setting '" + removedSetting + "'"));

    // Log disabled settings
    Collection<String> disabledSettings =
        settings.stream()
            .filter(setting -> !removedSettings.contains(setting))
            .filter(setting -> !plugin.getSettingsConfiguration().getSetting(setting).isEnabled())
            .collect(Collectors.toList());
    disabledSettings.forEach(
        disabledSetting -> plugin.getLogger().info("Disabled setting '" + disabledSetting + "'"));

    // Load settings
    settings.stream()
        .filter(setting -> !removedSettings.contains(setting))
        .filter(setting -> !disabledSettings.contains(setting))
        .map(setting -> plugin.getSettingsConfiguration().getSetting(setting))
        .forEach(
            setting -> {
              loadSetting(setting);
              plugin.getLogger().config("Registered setting '" + setting.getName() + "'");
            });
  }

  public void loadSettingsFromConfiguration() {
    plugin.getSettingsConfiguration().getSettingsFromConfiguration().forEach(this::loadSetting);
  }

  public boolean isSettingLoaded(String settingName) {
    return settingMap.containsKey(settingName);
  }

  private void loadSetting(Setting setting) {
    String settingName = setting.getName();
    setting.getListeners().forEach(plugin.getListenerManager()::registerListener);
    settingMap.putIfAbsent(settingName, setting);
  }

  private void unloadSetting(String settingName) {
    Optional.ofNullable(settingMap.remove(settingName))
        .map(Setting::getListeners)
        .ifPresent(listeners -> listeners.forEach(plugin.getListenerManager()::unregisterListener));
  }

  public void clear() {
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
