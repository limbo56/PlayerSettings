package me.limbo56.playersettings.settings;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
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
    // Unload loaded settings
    Collection<Setting> loadedSettings = new ArrayList<>(settingMap.values());
    loadedSettings.stream().map(Setting::getName).forEach(this::unloadSetting);

    // Filter out removed and disabled settings
    loadedSettings =
        loadedSettings.stream()
            .filter(
                setting -> {
                  String settingName = setting.getName();

                  // Log removed settings
                  if (!PlayerSettingsProvider.isSettingConfigured(settingName)) {
                    plugin.getLogger().info("Removed setting '" + settingName + "'");
                    return false;
                  }

                  // Log disabled settings
                  if (!PlayerSettingsProvider.getSettingByName(settingName).isEnabled()) {
                    plugin.getLogger().info("Disabled setting '" + settingName + "'");
                    return false;
                  }

                  return true;
                })
            .collect(Collectors.toList());

    // Register and log new settings
    List<String> loadedSettingNames =
        loadedSettings.stream().map(Setting::getName).collect(Collectors.toList());
    loadedSettings.addAll(
        plugin.getSettingsConfiguration().getEnabledSettings().stream()
            .filter(
                setting -> {
                  String settingName = setting.getName();

                  if (!loadedSettingNames.contains(settingName)) {
                    plugin.getLogger().info("Detected new setting '" + settingName + "'");
                    return true;
                  }

                  return false;
                })
            .map(
                setting -> {
                  // Restore default setting effects
                  Optional<Setting> optionalSetting =
                      DefaultSetting.getSettings().stream()
                          .filter(
                              defaultSetting -> defaultSetting.getName().equals(setting.getName()))
                          .findFirst();
                  if (optionalSetting.isPresent()) {
                    Setting defaultSetting = optionalSetting.get();
                    return ImmutableSetting.copyOf(setting)
                        .withListeners(defaultSetting.getListeners())
                        .withCallbacks(defaultSetting.getCallbacks());
                  }

                  return setting;
                })
            .collect(Collectors.toList()));

    // Load loaded settings
    loadedSettings.stream()
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
