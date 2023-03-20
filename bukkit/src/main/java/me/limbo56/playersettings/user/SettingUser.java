package me.limbo56.playersettings.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SettingUser {
  private static final List<Consumer<SettingUser>> loadCallbacks = new ArrayList<>();
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final UUID uuid;
  private final SettingWatcher settingWatcher;
  private boolean loading;
  private boolean flying;

  public SettingUser(UUID uuid) {
    this(uuid, new UserSettingsWatcher(uuid));
  }

  public SettingUser(UUID uuid, SettingWatcher settingWatcher) {
    this.uuid = uuid;
    this.loading = true;
    this.settingWatcher = settingWatcher;
  }

  public static void addLoadCallback(Consumer<SettingUser> loadCallback) {
    loadCallbacks.add(loadCallback);
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

  public boolean hasSettingPermissions(String settingName) {
    Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
    return Permissions.getSettingPermissionLevel(this.getPlayer(), setting) > 0;
  }

  public boolean isLoading() {
    return loading;
  }

  public void setLoading(boolean loading) {
    this.loading = loading;
    if (!loading) loadCallbacks.forEach(loadCallback -> loadCallback.accept(this));
  }

  public boolean isFlying() {
    return flying;
  }

  public void setFlying(boolean flying) {
    this.flying = flying;
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
