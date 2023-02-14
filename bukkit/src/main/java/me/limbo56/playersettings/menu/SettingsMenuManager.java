package me.limbo56.playersettings.menu;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.menu.renderers.DismissRenderer;
import me.limbo56.playersettings.menu.renderers.MenuPaginationRenderer;
import me.limbo56.playersettings.menu.renderers.MenuSettingsRenderer;
import me.limbo56.playersettings.menu.renderers.SettingsMenuItemRenderer;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Colors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SettingsMenuManager {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private static final SettingsMenuItemRenderer PAGINATION_RENDERER = new MenuPaginationRenderer();
  private static final SettingsMenuItemRenderer SETTING_RENDERER = new MenuSettingsRenderer();
  private static final SettingsMenuItemRenderer DISMISS_RENDERER = new DismissRenderer();
  private final LoadingCache<UUID, SettingsMenuHolder> menuHolderCache =
      CacheBuilder.newBuilder().build(new SettingsMenuHolderLoader());

  public void openMenu(SettingUser player, int page) {
    SettingsMenuHolder menuHolder = getSettingsMenuHolder(player.getUniqueId());
    menuHolder.getInventory().clear();
    PAGINATION_RENDERER.render(menuHolder, page);
    SETTING_RENDERER.render(menuHolder, page);
    DISMISS_RENDERER.render(menuHolder, page);
    player.getPlayer().openInventory(menuHolder.getInventory());
  }

  public void unload(UUID uuid) {
    menuHolderCache.invalidate(uuid);
  }

  public void unloadAll() {
    menuHolderCache.invalidateAll();
  }

  @NotNull
  private SettingsMenuHolder getSettingsMenuHolder(UUID uuid) {
    return menuHolderCache.getUnchecked(uuid);
  }

  private static class SettingsMenuHolderLoader extends CacheLoader<UUID, SettingsMenuHolder> {

    @Override
    public @NotNull SettingsMenuHolder load(@NotNull UUID uuid) {
      ConfigurationSection menuSection =
          PLUGIN.getPluginConfiguration().getFile().getConfigurationSection("menu");
      String menuName = Colors.translateColorCodes(menuSection.getString("name"));
      int menuSize = menuSection.getInt("size");
      return new SettingsMenuHolder(uuid, menuName, menuSize);
    }
  }
}
