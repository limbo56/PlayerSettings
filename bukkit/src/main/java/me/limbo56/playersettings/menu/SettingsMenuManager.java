package me.limbo56.playersettings.menu;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.menu.renderer.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsMenuManager {
  private final LoadingCache<UUID, SettingsMenu> settingsMenuCache;

  public SettingsMenuManager(PlayerSettings plugin) {
    this.settingsMenuCache = CacheBuilder.newBuilder().build(new SettingsMenuLoader(plugin));
  }

  public void open(Player player, int page) {
    SettingsMenu settingsMenu = settingsMenuCache.getUnchecked(player.getUniqueId());
    Inventory inventory = settingsMenu.getInventory();
    inventory.clear();

    for (Renderers renderer : Renderers.values()) {
      renderer.render(settingsMenu, page);
    }

    player.openInventory(inventory);
  }

  public void unload(UUID uuid) {
    settingsMenuCache.invalidate(uuid);
  }

  public void unloadAll() {
    settingsMenuCache.invalidateAll();
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

    public void render(SettingsMenu holder, int page) {
      this.renderer.render(holder, page);
    }
  }
}
