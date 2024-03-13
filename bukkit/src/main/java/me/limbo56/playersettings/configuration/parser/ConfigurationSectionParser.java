package me.limbo56.playersettings.configuration.parser;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigurationSectionParser<T> {
  T parse(ConfigurationSection section);
}
