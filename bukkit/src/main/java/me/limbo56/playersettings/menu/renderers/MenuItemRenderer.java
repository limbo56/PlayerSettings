package me.limbo56.playersettings.menu.renderers;

import me.limbo56.playersettings.menu.holder.MenuHolder;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MenuItemRenderer {

  void render(@NotNull MenuHolder menuHolder, int page);
}
