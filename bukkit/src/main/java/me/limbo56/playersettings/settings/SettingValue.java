package me.limbo56.playersettings.settings;

import me.limbo56.playersettings.util.data.Formatter;
import me.limbo56.playersettings.util.data.Parser;

public final class SettingValue implements Parser<String, Integer>, Formatter<Integer, String> {
  public static final SettingValue SETTING_VALUE = new SettingValue();

  @Override
  public Integer parse(String value) {
    if ("on".equalsIgnoreCase(value)) {
      return 1;
    } else if ("off".equalsIgnoreCase(value)) {
      return 0;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  public String format(Integer value) {
    return value == 1 ? "on" : value < 1 ? "off" : String.valueOf(value);
  }
}
