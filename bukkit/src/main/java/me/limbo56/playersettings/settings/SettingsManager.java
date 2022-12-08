package me.limbo56.playersettings.settings;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsContainer;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
import me.limbo56.playersettings.api.setting.Setting;
import org.jetbrains.annotations.NotNull;

public class SettingsManager implements SettingsContainer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Set<Setting> customSettings;
  private final ConcurrentMap<String, Setting> settingMap;

  public SettingsManager() {
    this.customSettings = new HashSet<>();
    this.settingMap = new ConcurrentHashMap<>();
  }

  @Override
  public void registerSetting(Setting setting) {
    String settingName =
        Preconditions.checkNotNull(setting.getName(), "Setting name cannot be null");
    if (settingMap.containsKey(settingName)) {
      String message =
          "Skipping registration of setting '"
              + settingName
              + "'. A setting with the same name is already registered!";
      PLUGIN.getLogger().config(message);
      return;
    }
    PlayerSettingsProvider.configureCustomSetting(setting);

    Setting parsedSetting = mergeWithConfiguration(setting);
    if (parsedSetting == null || !parsedSetting.isEnabled()) {
      String message =
          "Skipping registration of setting '"
              + settingName
              + "'. Missing or not enabled in configuration.";
      PLUGIN.getLogger().config(message);
      return;
    }
    this.loadSetting(parsedSetting);
    this.customSettings.add(parsedSetting);
    PLUGIN.getLogger().config("Registered setting '" + setting.getName() + "'");
  }

  public void loadSetting(Setting setting) {
    String settingName = setting.getName();
    setting.getListeners().forEach(PLUGIN.getListenerManager()::registerListener);
    settingMap.putIfAbsent(settingName, setting);
  }

  private void unloadSetting(String settingName) {
    Optional.ofNullable(settingMap.remove(settingName))
        .map(Setting::getListeners)
        .ifPresent(listeners -> listeners.forEach(PLUGIN.getListenerManager()::unregisterListener));
  }

  public void reloadSettings() {
    Collection<Setting> configurationSettings =
        PLUGIN.getSettingsConfiguration().getEnabledSettings();
    Map<String, Setting> loadedSettings = new HashMap<>(settingMap);
    Collection<String> loadedSettingNames = new ArrayList<>(loadedSettings.keySet());
    loadedSettingNames.forEach(this::unloadSetting);

    Collection<String> configurationSettingNames =
        configurationSettings.stream().map(Setting::getName).collect(Collectors.toList());
    Collection<String> removedSettingNames =
        loadedSettingNames.stream()
            .filter(name -> configurationSettingNames.stream().noneMatch(name::equals))
            .collect(Collectors.toList());
    removedSettingNames.forEach(
        setting -> PLUGIN.getLogger().info("Removed setting '" + setting + "'"));

    Collection<String> newSettingNames =
        configurationSettingNames.stream()
            .filter(name -> loadedSettingNames.stream().noneMatch(name::equals))
            .collect(Collectors.toList());
    newSettingNames.forEach(
        setting -> PLUGIN.getLogger().info("Detected new setting '" + setting + "'"));
    loadedSettingNames.addAll(newSettingNames);

    Function<String, Setting> reloadedSettingMapper =
        createReloadedSettingMapper(configurationSettings);
    loadedSettingNames.stream()
        .filter(settingName -> !removedSettingNames.contains(settingName))
        .map(reloadedSettingMapper)
        .forEach(
            setting -> {
              loadSetting(setting);
              PLUGIN.getLogger().config("Registered setting '" + setting.getName() + "'");
            });
  }

  @NotNull
  private Function<String, Setting> createReloadedSettingMapper(Collection<Setting> settingList) {
    return settingName ->
        findSetting(customSettings, settingName)
            .map(this::mergeWithConfiguration)
            .orElse(
                findSetting(settingList, settingName)
                    .orElseThrow(
                        () ->
                            new NoSuchElementException(
                                "Could not find setting '" + settingName + "'")));
  }

  private Setting mergeWithConfiguration(Setting setting) {
    Setting configuredSetting = PLUGIN.getSettingsConfiguration().parseSetting(setting.getName());
    return ImmutableSetting.copyOf(configuredSetting)
        .withListeners(setting.getListeners())
        .withCallbacks(setting.getCallbacks());
  }

  @NotNull
  private Optional<Setting> findSetting(Collection<Setting> list, String settingName) {
    return list.stream().filter(setting -> setting.getName().equals(settingName)).findAny();
  }

  public boolean isSettingLoaded(String settingName) {
    return settingMap.containsKey(settingName);
  }

  public void unloadAll() {
    customSettings.clear();
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
