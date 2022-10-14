package me.limbo56.playersettings.user;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.SettingsWatchlist;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SettingUserManager implements SettingsWatchlist {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private final LoadingCache<UUID, SettingUser> holderCache =
      CacheBuilder.newBuilder().build(new SettingUserLoader());

  public void loadUser(UUID owner) {
    this.getUser(owner).loadSettings();
  }

  public void loadOnlineUsers() {
    List<SettingUser> users =
        Bukkit.getOnlinePlayers().stream()
            .map(Entity::getUniqueId)
            .map(this::getUser)
            .collect(Collectors.toList());
    plugin.getDataManager().loadUsers(users);
  }

  public void unloadUser(UUID owner) {
    this.getUser(owner).saveSettings();
    holderCache.invalidate(owner);
  }

  public void unloadAllUsers() {
    plugin.getDataManager().saveUsers(this.getUsers());
    this.invalidateAll();
  }

  @Override
  public SettingWatcher getSettingWatcher(UUID uuid) {
    return this.getUser(uuid).getSettingWatcher();
  }

  public SettingUser getUser(UUID owner) {
    return holderCache.getUnchecked(owner);
  }

  public Collection<SettingUser> getUsers() {
    return holderCache.asMap().values();
  }

  public void invalidateAll() {
    holderCache.invalidateAll();
  }

  private static class SettingUserLoader extends CacheLoader<UUID, SettingUser> {
    @Override
    public @NotNull SettingUser load(@NotNull UUID uuid) {
      return new SettingUser(uuid, new SimpleSettingWatcher(uuid));
    }
  }
}
