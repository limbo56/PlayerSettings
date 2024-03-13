package me.limbo56.playersettings.configuration.parser;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import org.bukkit.configuration.ConfigurationSection;

public class ValueAliasesParser
    implements ConfigurationSectionParser<ImmutableListMultimap<String, Integer>> {
  public ImmutableListMultimap<String, Integer> parse(ConfigurationSection section) {
    ListMultimap<String, Integer> valueAliases = LinkedListMultimap.create();
    if (section != null) {
      for (String key : section.getKeys(false)) {
        int value = Integer.parseInt(key);
        if (section.isList(key)) {
          section.getStringList(key).forEach(alias -> valueAliases.put(alias, value));
        } else if (section.isString(key)) {
          valueAliases.put(section.getString(key), value);
        }
      }
    }
    return ImmutableListMultimap.copyOf(valueAliases);
  }
}
