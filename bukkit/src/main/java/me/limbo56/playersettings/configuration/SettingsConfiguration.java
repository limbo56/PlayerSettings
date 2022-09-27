package me.limbo56.playersettings.configuration;

import static me.limbo56.playersettings.settings.SettingParser.SETTING_PARSER;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.util.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsConfiguration implements PluginConfiguration {
  private static final Collection<String> CUSTOM_SETTING_PROPERTIES =
      Arrays.asList("onEnable", "onDisable", "values");

  public void configureSetting(Setting setting) {
    YamlConfiguration settingsConfiguration = getFile();
    String settingName = setting.getName();
    ConfigUtil.configureSerializable(settingsConfiguration, settingName, setting);

    // Save changes
    try {
      File pluginFile = ConfigUtil.getPluginFile(getFileName());
      settingsConfiguration.save(pluginFile);
    } catch (IOException e) {
      PlayerSettingsProvider.getPlugin()
          .getLogger()
          .severe("Failed while saving setting '" + settingName + "' to configuration");
      e.printStackTrace();
    }
  }

  public boolean hasSetting(String settingName) {
    return getFile().contains(settingName);
  }

  public Setting getSetting(Setting setting) {
    Setting parsedSetting = getSetting(setting.getName());
    return ImmutableSetting.copyOf(parsedSetting)
        .withListeners(setting.getListeners())
        .withCallbacks(setting.getCallbacks());
  }

  public Setting getSetting(String settingName) {
    return SETTING_PARSER.parse(getFile().getConfigurationSection(settingName));
  }

  public Collection<Setting> getSettingsFromConfiguration() {
    YamlConfiguration settingsConfiguration = getFile();
    return settingsConfiguration.getKeys(false).stream()
        .map(this::getSetting)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public SettingType getSettingType(ConfigurationSection section) {
    Collection<String> sectionKeys = section.getKeys(false);
    if (sectionKeys.containsAll(CUSTOM_SETTING_PROPERTIES)) {
      return SettingType.CUSTOM_SETTING;
    } else {
      return SettingType.SETTING;
    }
  }

  @Override
  public String getFileName() {
    return "settings.yml";
  }

  private enum SettingType {
    CUSTOM_SETTING,
    SETTING
  }
}
