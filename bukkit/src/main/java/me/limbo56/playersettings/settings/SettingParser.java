package me.limbo56.playersettings.settings;

import static me.limbo56.playersettings.menu.MenuItemParser.MENU_ITEM_PARSER;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingCallback;
import me.limbo56.playersettings.util.data.Parser;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class SettingParser implements Parser<ConfigurationSection, Setting> {
  public static final SettingParser SETTING_PARSER = new SettingParser();
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @Override
  public Setting parse(ConfigurationSection settingSection) {
    String settingName = settingSection.getName();
    MenuItem menuItem = parseSettingItem(settingName);
    Collection<SettingCallback> callbacks = parseSettingCallbacks(settingSection);
    return ImmutableSetting.builder()
        .name(settingName)
        .enabled(settingSection.getBoolean("enabled", true))
        .defaultValue(settingSection.getInt("default", 0))
        .maxValue(settingSection.getInt("max", 1))
        .executeOnJoin(settingSection.getBoolean("executeOnJoin", true))
        .addAllCallbacks(callbacks)
        .item(menuItem)
        .build();
  }

  @NotNull
  private MenuItem parseSettingItem(String settingName) {
    ConfigurationSection itemSection =
        Preconditions.checkNotNull(
            plugin.getItemsConfiguration().getConfigurationSection(settingName),
            "No item section found for setting '" + settingName + "'");
    return Preconditions.checkNotNull(
        MENU_ITEM_PARSER.parse(itemSection),
        "Item for setting '" + itemSection.getName() + "' not found");
  }

  @NotNull
  private Collection<SettingCallback> parseSettingCallbacks(ConfigurationSection settingSection) {
    Map<String, List<String>> commandMap = new HashMap<>();
    if (!settingSection.contains("values")) {
      if (!settingSection.contains("onEnable") && !settingSection.contains("onDisable")) {
        return new HashSet<>();
      }

      commandMap.put("1", settingSection.getStringList("onEnable"));
      commandMap.put("0", settingSection.getStringList("onDisable"));
    } else {
      ConfigurationSection valuesSection = settingSection.getConfigurationSection("values");
      commandMap =
          valuesSection.getKeys(false).stream()
              .collect(Collectors.toMap(value -> value, valuesSection::getStringList));
    }
    return Sets.newHashSet(new CustomSettingCallback(commandMap));
  }
}
