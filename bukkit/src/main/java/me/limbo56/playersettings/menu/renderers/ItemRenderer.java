package me.limbo56.playersettings.menu.renderers;

import me.limbo56.playersettings.menu.SettingsInventory;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ItemRenderer {
  void render(@NotNull SettingsInventory inventory, int page);
}
