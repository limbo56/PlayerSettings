package me.limbo56.playersettings.configuration;

import java.io.IOException;
import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class SettingsConfiguration extends BaseConfiguration {
  private final ItemsConfiguration itemsConfiguration;

  public SettingsConfiguration(PlayerSettings plugin) {
    super(plugin);
    this.itemsConfiguration =
        plugin.getConfigurationManager().getConfiguration(ItemsConfiguration.class);
  }

  @Override
  @NotNull
  public String getFileName() {
    return "settings.yml";
  }

  public void writeSetting(Setting setting) {
    String settingName = setting.getName();
    try {
      this.writeSerializable(settingName, setting);
      this.save();
    } catch (IOException exception) {
      PluginLogger.severe(
          "Failed to save setting '" + settingName + "' to configuration", exception);
    }
  }

  public boolean isSaveFlightStateEnabled() {
    return configuration.getBoolean(Settings.fly().getName() + ".save-state", true);
  }

  public boolean isForceFlightOnJoinEnabled() {
    return configuration.getBoolean(Settings.fly().getName() + ".force-on-join", false);
  }

  public boolean isSettingConfigured(String settingName) {
    return configuration.contains(settingName);
  }

  public boolean isSettingEnabled(String settingName) {
    return configuration.getBoolean(settingName + ".enabled");
  }

  public Set<String> getSettingNames() {
    return configuration.getKeys(false);
  }

  public ConfigurationSection getSettingOverridesSection(String settingName) {
    return configuration.getConfigurationSection(settingName + ".overrides");
  }

  public Collection<Setting> getEnabledSettings(boolean verbose) {
    List<Setting> enabledSettings = new ArrayList<>();
    for (String settingName : configuration.getKeys(false)) {
      if (isEnabled(settingName, verbose)) {
        enabledSettings.add(parseSetting(settingName));
      }
    }
    return enabledSettings;
  }

  private boolean isEnabled(String settingName, boolean verbose) {
    boolean hasItemConfigured = itemsConfiguration.isItemConfigured(settingName);
    if (!hasItemConfigured && verbose) {
      PluginLogger.warning(
          "No item definition found for setting '" + settingName + "'! Skipping registration...");
    }
    return isSettingEnabled(settingName) && hasItemConfigured;
  }

  private Setting parseSetting(String settingName) {
    ConfigurationSection section =
        Objects.requireNonNull(
            configuration.getConfigurationSection(settingName),
            "Missing configuration for setting '" + settingName + "' in 'settings.yml'");
    return Parsers.SETTING_PARSER.parse(section);
  }
}
