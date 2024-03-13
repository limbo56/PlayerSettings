package me.limbo56.playersettings.util;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

public final class Adventure {
  private static final @NotNull LegacyComponentSerializer LEGACY_SECTION_SERIALIZER =
      LegacyComponentSerializer.legacySection();
  private static final @NotNull LegacyComponentSerializer LEGACY_AMPERSAND_SERIALIZER =
      LegacyComponentSerializer.legacyAmpersand();

  private Adventure() {}

  public static TextComponent fromLegacySection(String text) {
    return LEGACY_SECTION_SERIALIZER.deserialize(text);
  }

  public static TextComponent fromLegacyAmpersand(String text) {
    return LEGACY_AMPERSAND_SERIALIZER.deserialize(text);
  }

  public static TextReplacementConfig createReplacement(
      @RegExp String pattern, TextComponent replacement) {
    return TextReplacementConfig.builder().match(pattern).replacement(replacement).build();
  }
}
