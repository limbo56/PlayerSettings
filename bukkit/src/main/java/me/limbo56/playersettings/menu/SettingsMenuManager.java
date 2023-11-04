package me.limbo56.playersettings.menu;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.UUID;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.holder.MenuHolderLoader;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SettingsMenuManager {
  private final LoadingCache<UUID, MenuHolder> menuHolderCache =
      CacheBuilder.newBuilder().build(new MenuHolderLoader());

  public void unload(UUID uuid) {
    menuHolderCache.invalidate(uuid);
  }

  public void unloadAll() {
    menuHolderCache.invalidateAll();
  }

  @NotNull
  public MenuHolder getMenuHolder(Player player) {
    return getMenuHolder(player.getUniqueId());
  }

  @NotNull
  public MenuHolder getMenuHolder(UUID uuid) {
    return menuHolderCache.getUnchecked(uuid);
  }
}
