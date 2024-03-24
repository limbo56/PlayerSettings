package me.limbo56.playersettings.configuration.parser;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationSectionParseException extends RuntimeException {
  private final ConfigurationSection section;

  public ConfigurationSectionParseException(
      String message, ConfigurationSection section, Throwable cause) {
    super(message, cause);
    this.section = section;
  }

  public ConfigurationSectionParseException(String message, Throwable cause) {
    super(message, cause);
    this.section = null;
  }

  public ConfigurationSection getSection() {
    return section;
  }
}
