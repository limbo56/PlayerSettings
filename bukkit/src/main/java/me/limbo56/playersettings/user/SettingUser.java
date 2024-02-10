package me.limbo56.playersettings.user;

import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.setting.CachedSettingWatcher;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SettingUser {
  private final SettingsManager settingsManager;
  private final UUID uuid;
  private final SettingWatcher settingWatcher;
  private boolean loading;
  private boolean flying;

  public SettingUser(UUID uuid) {
    this(uuid, new CachedSettingWatcher(uuid));
  }

  public SettingUser(UUID uuid, SettingWatcher settingWatcher) {
    this.settingsManager = PlayerSettings.getInstance().getSettingsManager();
    this.uuid = uuid;
    this.loading = true;
    this.settingWatcher = settingWatcher;
  }

  public void clearSettingEffects() {
    for (String settingName : settingWatcher.getWatched()) {
      Setting setting = settingsManager.getSetting(settingName);
      if (setting != null) {
        clearSettingEffects(setting);
      }
    }
  }

  private void clearSettingEffects(Setting setting) {
    for (SettingCallback callback : setting.getCallbacks()) {
      callback.clear(getPlayer());
    }
  }

  public boolean isLoading() {
    return loading;
  }

  public void setLoading(boolean loading) {
    this.loading = loading;
  }

  public boolean isFlying() {
    return flying;
  }

  public void setFlying(boolean flying) {
    this.flying = flying;
  }

  public boolean hasSettingEnabled(String settingName) {
    return settingWatcher.getValue(settingName) > 0;
  }

  public boolean hasSettingPermissions(String settingName) {
    Setting setting = settingsManager.getSetting(settingName);
    return Permissions.getSettingPermissionLevel(this.getPlayer(), setting) > 0;
  }

  public Player getPlayer() {
    return Bukkit.getServer().getPlayer(uuid);
  }

  public SettingWatcher getSettingWatcher() {
    return settingWatcher;
  }

  public Collection<String> getAllowedSettings() {
    List<String> allowedSettings = new ArrayList<>();
    for (String settingName : settingsManager.getSettingNames()) {
      if (hasSettingPermissions(settingName)) {
        allowedSettings.add(settingName);
      }
    }
    return allowedSettings;
  }
}
