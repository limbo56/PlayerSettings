package me.limbo56.playersettings.configuration.parser;

import static me.limbo56.playersettings.configuration.parser.Parsers.SETTING_ITEM_PARSER;
import static me.limbo56.playersettings.configuration.parser.Parsers.VALUE_ALIASES_PARSER;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.HashSet;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.ImmutableSetting;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class SettingParser implements ConfigurationSectionParser<Setting> {
  public Setting parse(ConfigurationSection section) {
    String settingName = section.getName();
    ConfigurationSection itemSection = getItemSection(settingName);
    HashSet<SettingCallback> callbacks = parseCallbacks(section);

    return ImmutableSetting.builder()
        .name(settingName)
        .displayName(section.getString("name", settingName))
        .enabled(section.getBoolean("enabled", true))
        .defaultValue(section.getInt("default", 0))
        .maxValue(section.getInt("max", 1))
        .triggers(section.getStringList("triggers").toArray(new String[0]))
        .item(SETTING_ITEM_PARSER.parse(itemSection))
        .valueAliases(VALUE_ALIASES_PARSER.parse(section.getConfigurationSection("overrides")))
        .callbacks(callbacks)
        .build();
  }

  @NotNull
  private static HashSet<SettingCallback> parseCallbacks(ConfigurationSection section) {
    HashSet<SettingCallback> callbacks = Sets.newHashSet();
    SettingCallback callback = Parsers.CALLBACK_PARSER.parse(section);
    if (callback != null) {
      callbacks.add(callback);
    }
    return callbacks;
  }

  @NotNull
  private static ConfigurationSection getItemSection(String settingName) {
    return Preconditions.checkNotNull(
        PlayerSettings.getInstance()
            .getConfigurationManager()
            .getConfiguration(ItemsConfiguration.class)
            .getFile()
            .getConfigurationSection(settingName),
        "Failed to find setting item '" + settingName + "'");
  }
}
