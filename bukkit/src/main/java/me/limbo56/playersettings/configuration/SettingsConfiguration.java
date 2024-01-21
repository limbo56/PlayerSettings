package me.limbo56.playersettings.configuration;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingCallback;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.settings.CustomSettingCallback;
import me.limbo56.playersettings.settings.DefaultSettings;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class SettingsConfiguration extends BaseConfiguration {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  private static ItemsConfiguration getItemsConfiguration() {
    return PLUGIN.getItemsConfiguration();
  }

  public Setting parseSetting(String settingName) {
    ConfigurationSection settingSection =
        Preconditions.checkNotNull(
            getFile().getConfigurationSection(settingName),
            "Failed to find setting '" + settingName + "'");
    ConfigurationSection itemSection =
        Preconditions.checkNotNull(
            getItemsConfiguration().getFile().getConfigurationSection(settingName),
            "Failed to find setting item '" + settingName + "'");

    HashSet<SettingCallback> callbacks = Sets.newHashSet();
    SettingCallback deserialize = CustomSettingCallback.deserialize(settingSection);
    if (deserialize != null) {
      callbacks = Sets.newHashSet(deserialize);
    }
    return ImmutableSetting.copyOf(Setting.deserialize(settingSection))
        .withItem(SettingsMenuItem.deserialize(itemSection, 0))
        .withCallbacks(callbacks);
  }

  public Setting parseSetting(Setting setting) {
    return mergeSettingWithConfiguration(configureSetting(setting));
  }

  private Setting configureSetting(Setting setting) {
    String settingName = setting.getName();
    if (!isSettingConfigured(settingName)) {
      writeSetting(setting);
    }

    ItemsConfiguration itemsConfiguration = getItemsConfiguration();
    if (!itemsConfiguration.isItemConfigured(settingName)) {
      itemsConfiguration.writeMenuItem(settingName, setting.getItem());
    }

    return setting;
  }

  private void writeSetting(Setting setting) {
    String settingName = setting.getName();
    try {
      this.writeSerializable(settingName, setting);
      this.save();
    } catch (IOException exception) {
      PluginLogger.severe("Failed to save setting '" + settingName + "' to configuration");
      exception.printStackTrace();
    }
  }

  public Setting mergeSettingWithConfiguration(Setting setting) {
    Setting parsedSetting = parseSetting(setting.getName());
    return ImmutableSetting.copyOf(parsedSetting)
        .withCallbacks(setting.getCallbacks())
        .withListeners(setting.getListeners())
        .withValueAliases(getSettingValueAliases(parsedSetting));
  }

  public ConfigurationSection getSettingOverridesSection(String settingName) {
    return getFile().getConfigurationSection(settingName + ".overrides");
  }

  public Integer parseSettingValue(Setting setting, String value) {
    try {
      int settingValue =
          getSettingValueAliases(setting).get(value).stream()
              .findFirst()
              .orElseGet(() -> Integer.parseInt(value));
      PluginLogger.debug(
          "Parsing value '"
              + value
              + "' for setting '"
              + setting.getName()
              + "', Parsed '"
              + settingValue
              + "'");
      return settingValue;
    } catch (NumberFormatException exception) {
      PluginLogger.debug("Unknown value '" + value + "' for setting '" + setting.getName() + "'");
      return null;
    }
  }

  public String formatSettingValue(Setting setting, int value) {
    int key = value < 1 ? 0 : value;
    return ImmutableListMultimap.copyOf(getSettingValueAliases(setting)).inverse().get(key).stream()
        .findFirst()
        .orElse(String.valueOf(value));
  }

  private Multimap<String, Integer> getSettingValueAliases(Setting parsedSetting) {
    Multimap<String, Integer> settingAliasMap = parsedSetting.getValueAliases();
    if (settingAliasMap != null && !settingAliasMap.isEmpty()) {
      return settingAliasMap;
    }

    ListMultimap<String, Integer> pluginAliasMap =
        PLUGIN.getPluginConfiguration().getValueAliases();
    if (pluginAliasMap != null) {
      return pluginAliasMap;
    }

    return DefaultSettings.Constants.DEFAULT_VALUE_ALIASES;
  }

  public Collection<Setting> getEnabledSettings(boolean verbose) {
    Predicate<String> predicate = filterAndLogEnabledSettings(verbose);
    List<Setting> list = new ArrayList<>();

    for (String settingName : getFile().getKeys(false)) {
      if (predicate.test(settingName)) {
        Setting setting = parseSetting(settingName);
        list.add(setting);
      }
    }

    return list;
  }

  @NotNull
  private Predicate<String> filterAndLogEnabledSettings(boolean verbose) {
    YamlConfiguration file = getFile();
    ItemsConfiguration itemsConfiguration = getItemsConfiguration();
    return settingName -> {
      boolean hasItemConfigured = itemsConfiguration.isItemConfigured(settingName);
      if (!hasItemConfigured && verbose) {
        PLUGIN
            .getLogger()
            .warning(
                "No item definition found for setting '"
                    + settingName
                    + "'! Skipping registration...");
      }
      return file.getBoolean(settingName + ".enabled") && hasItemConfigured;
    };
  }

  public Set<String> getSettingNames() {
    return getFile().getKeys(false);
  }

  public boolean isSettingEnabled(String settingName) {
    return getFile().getBoolean(settingName + ".enabled");
  }

  public boolean isSettingConfigured(String settingName) {
    return getFile().contains(settingName);
  }

  @NotNull
  public String getFileName() {
    return "settings.yml";
  }
}
