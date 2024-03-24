package me.limbo56.playersettings.configuration.parser;

import com.google.common.collect.ImmutableListMultimap;
import org.bukkit.configuration.ConfigurationSection;

public class ValueAliasesParser
    implements ConfigurationSectionParser<ImmutableListMultimap<String, Integer>> {
  public ImmutableListMultimap<String, Integer> parse(ConfigurationSection section) {
    if (section == null) {
      return ImmutableListMultimap.of();
    }

    ImmutableListMultimap.Builder<String, Integer> builder = ImmutableListMultimap.builder();
    for (String key : section.getKeys(false)) {
      int value = Integer.parseInt(key);
      if (section.isList(key)) {
        section.getStringList(key).forEach(alias -> builder.put(alias, value));
      } else if (section.isString(key)) {
        builder.put(section.getString(key), value);
      }
    }

    return builder.build();
  }
}
