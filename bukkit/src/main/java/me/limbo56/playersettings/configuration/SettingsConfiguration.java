package me.limbo56.playersettings.configuration;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingCallback;
import me.limbo56.playersettings.settings.CustomSettingCallback;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SettingsConfiguration extends BaseConfiguration {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public void configureSetting(Setting setting) {
    String settingName = setting.getName();
    try {
      this.writeSerializable(settingName, setting);
      this.save();
    } catch (IOException e) {
      PLUGIN.getLogger().severe("Failed to save setting '" + settingName + "' to configuration");
      e.printStackTrace();
    }
  }

  public Setting parseSetting(String settingName) {
    ConfigurationSection section =
        Preconditions.checkNotNull(
            getFile().getConfigurationSection(settingName),
            "Failed to find setting '" + settingName + "'");
    Setting setting = Setting.deserialize(section);
    MenuItem menuItem = PLUGIN.getItemsConfiguration().getMenuItem(settingName);
    Collection<SettingCallback> callbacks = parseSettingCallbacks(section);
    return ImmutableSetting.copyOf(setting).withItem(menuItem).withCallbacks(callbacks);
  }

  private Collection<SettingCallback> parseSettingCallbacks(ConfigurationSection section) {
    Map<String, List<String>> commandMap = new HashMap<>();
    if (!section.contains("values")) {
      if (!section.contains("onEnable") && !section.contains("onDisable")) {
        return new HashSet<>();
      }

      commandMap.put("1", section.getStringList("onEnable"));
      commandMap.put("0", section.getStringList("onDisable"));
    } else {
      ConfigurationSection valuesSection = section.getConfigurationSection("values");
      commandMap =
          valuesSection.getKeys(false).stream()
              .collect(Collectors.toMap(value -> value, valuesSection::getStringList));
    }
    return Sets.newHashSet(new CustomSettingCallback(commandMap));
  }

  public Collection<Setting> getEnabledSettings() {
    return getFile().getKeys(false).stream()
        .filter(
            settingName -> {
              boolean isEnabled = getFile().getBoolean(settingName + ".enabled");
              boolean hasItemConfigured =
                  PLUGIN.getItemsConfiguration().isItemConfigured(settingName);
              if (!hasItemConfigured) {
                PLUGIN
                    .getLogger()
                    .warning(
                        "No item definition found for setting '"
                            + settingName
                            + "'! Skipping registration...");
              }
              return isEnabled && hasItemConfigured;
            })
        .map(this::parseSetting)
        .collect(Collectors.toList());
  }

  public boolean isSettingConfigured(String settingName) {
    return getFile().contains(settingName);
  }

  public ConfigurationSection getSettingOverridesSection(String settingName) {
    return getFile().getConfigurationSection(settingName + ".overrides");
  }

  @NotNull
  public String getFileName() {
    return "settings.yml";
  }
}
