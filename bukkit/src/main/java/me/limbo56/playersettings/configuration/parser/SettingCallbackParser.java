package me.limbo56.playersettings.configuration.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.limbo56.playersettings.setting.callback.CustomSettingCallback;
import org.bukkit.configuration.ConfigurationSection;

public final class SettingCallbackParser
    implements ConfigurationSectionParser<CustomSettingCallback> {
  public CustomSettingCallback parse(ConfigurationSection section) {
    Map<String, List<String>> commandMap = new HashMap<>();
    if (!section.contains("values")
        && !section.contains("onEnable")
        && !section.contains("onDisable")) {
      return null;
    }

    ConfigurationSection valuesSection = section.getConfigurationSection("values");
    if (valuesSection != null) {
      commandMap =
          valuesSection.getKeys(false).stream()
              .collect(Collectors.toMap(value -> value, valuesSection::getStringList));
    } else if (section.contains("onEnable") || section.contains("onDisable")) {
      commandMap.put("1", section.getStringList("onEnable"));
      commandMap.put("0", section.getStringList("onDisable"));
    }

    return new CustomSettingCallback(commandMap);
  }
}
