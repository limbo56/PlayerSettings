package me.limbo56.playersettings.menu.renderer;

import me.limbo56.playersettings.menu.SettingsMenu;
import org.jetbrains.annotations.NotNull;

public interface MenuItemRenderer {
  void render(@NotNull SettingsMenu settingsMenu, int page);
}
