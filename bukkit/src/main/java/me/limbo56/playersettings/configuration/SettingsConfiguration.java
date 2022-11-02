package me.limbo56.playersettings.configuration;

import static me.limbo56.playersettings.settings.SettingParser.SETTING_PARSER;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
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

  public Setting getConfiguredSetting(Setting setting) {
    Setting parsedSetting = getSettingByName(setting.getName());
    return ImmutableSetting.copyOf(parsedSetting)
      .withListeners(setting.getListeners())
      .withCallbacks(setting.getCallbacks());
  }

  public Setting getSettingByName(String settingName) {
    ConfigurationSection section =
      Preconditions.checkNotNull(
        getFile().getConfigurationSection(settingName),
        "Failed to find setting '" + settingName + "'");
    return SETTING_PARSER.parse(section);
  }

  public Collection<Setting> getEnabledSettings() {
    YamlConfiguration settingsConfiguration = getFile();
    return settingsConfiguration.getKeys(false).stream()
      .map(this::getSettingByName)
      .filter((setting) -> Objects.nonNull(setting) && setting.isEnabled())
      .collect(Collectors.toList());
  }

  @Override
  public String getFileName() {
    return "settings.yml";
  }
}
