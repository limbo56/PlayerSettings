package me.limbo56.playersettings.menu.renderers;

import me.limbo56.playersettings.menu.SettingsMenuHolder;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SettingsMenuItemRenderer {

  void render(@NotNull SettingsMenuHolder menuHolder, int page);
}
