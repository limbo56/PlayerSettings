package me.limbo56.playersettings.menu;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.renderers.*;
import org.bukkit.entity.Player;

public final class SettingsMenu {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public static void open(Player player, int page) {
    MenuHolder menuHolder = PLUGIN.getSettingsMenuManager().getMenuHolder(player);
    menuHolder.getInventory().clear();

    for (Renderers renderer : Renderers.values()) {
      renderer.render(menuHolder, page);
    }

    player.openInventory(menuHolder.getInventory());
  }

  private enum Renderers {
    SETTINGS(new SettingsRenderer()),
    PAGINATION(new PaginationRenderer()),
    DISMISS_BUTTON(new DismissRenderer()),
    CUSTOM_ITEMS(new CustomItemsRenderer());

    final MenuItemRenderer renderer;

    Renderers(MenuItemRenderer renderer) {
      this.renderer = renderer;
    }

    public void render(MenuHolder holder, int page) {
      this.renderer.render(holder, page);
    }
  }
}
