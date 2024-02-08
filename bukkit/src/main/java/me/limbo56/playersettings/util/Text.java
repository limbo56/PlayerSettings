package me.limbo56.playersettings.util;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class Text {
  private static final @NotNull LegacyComponentSerializer LEGACY_SECTION_SERIALIZER =
      LegacyComponentSerializer.legacySection();
  private static final @NotNull LegacyComponentSerializer LEGACY_AMPERSAND_SERIALIZER =
      LegacyComponentSerializer.legacyAmpersand();

  private Text() {}

  public static TextComponent fromLegacySection(String text) {
    return LEGACY_SECTION_SERIALIZER.deserialize(text);
  }

  public static TextComponent fromLegacyAmpersand(String text) {
    return LEGACY_AMPERSAND_SERIALIZER.deserialize(text);
  }
}
