package me.limbo56.playersettings.message.text;

import java.util.function.Function;

public class ReplaceModifier implements Function<String, String> {
  private final String pattern;
  private final String value;

  public ReplaceModifier(String pattern, String value) {
    this.pattern = pattern;
    this.value = value;
  }

  public static ReplaceModifier of(String pattern, String replacement) {
    return new ReplaceModifier(pattern, replacement);
  }

  @Override
  public String apply(String text) {
    return text.replaceAll(pattern, value);
  }
}
