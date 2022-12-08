package me.limbo56.playersettings.user;

import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SettingUser {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final UUID uuid;
  private SettingWatcher settingWatcher;
  private boolean loading;

  public SettingUser(UUID uuid) {
    this(uuid, new UserSettingsWatcher(uuid));
  }

  public SettingUser(UUID uuid, SettingWatcher settingWatcher) {
    this.uuid = uuid;
    this.loading = true;
    this.settingWatcher = settingWatcher;
  }

  public void clearSettingEffects() {
    PLUGIN.getSettingsManager().getSettingMap().values().forEach(this::clearSettingEffects);
  }

  private void clearSettingEffects(Setting setting) {
    setting.getCallbacks().forEach(callback -> callback.clear(getPlayer()));
  }

  public boolean hasSettingEnabled(String settingName) {
    return settingWatcher.getValue(settingName) > 0;
  }

  public boolean hasSettingPermissions(@NotNull String settingName) {
    Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
    return PlayerSettingsProvider.getSettingPermissionLevel(this.getPlayer(), setting) > 0;
  }

  public void setSettingWatcher(SettingWatcher watcher) {
    this.settingWatcher = watcher;
  }

  public void setLoading(boolean loading) {
    this.loading = loading;
  }

  public boolean isLoading() {
    return loading;
  }

  public UUID getUniqueId() {
    return uuid;
  }

  public Player getPlayer() {
    return Bukkit.getServer().getPlayer(uuid);
  }

  public SettingWatcher getSettingWatcher() {
    return settingWatcher;
  }
}
