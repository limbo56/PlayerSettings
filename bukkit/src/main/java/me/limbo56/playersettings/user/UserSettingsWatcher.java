package me.limbo56.playersettings.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.api.event.SettingUpdateEvent;
import me.limbo56.playersettings.setting.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserSettingsWatcher implements SettingWatcher {
  private final SettingsManager settingsManager;
  private final UUID uuid;
  private final Map<String, Integer> settingMap;

  public UserSettingsWatcher(UUID owner) {
    this.settingsManager = PlayerSettings.getInstance().getSettingsManager();
    this.uuid = owner;
    this.settingMap = new HashMap<>();
  }

  @Override
  public int getValue(@NotNull String settingName) {
    int defaultValue = settingsManager.getSetting(settingName).getDefaultValue();
    return settingMap.getOrDefault(settingName, defaultValue);
  }

  @Override
  public void setValue(@NotNull String settingName, int value, boolean silent) {
    Integer previousValue = Optional.ofNullable(settingMap.put(settingName, value)).orElse(0);
    if (!silent) {
      Setting setting = settingsManager.getSetting(settingName);
      Player player = Bukkit.getPlayer(getOwner());
      Bukkit.getPluginManager()
          .callEvent(new SettingUpdateEvent(player, setting, previousValue, value));
      setting.getCallbacks().forEach(callback -> callback.notifyChange(player, setting, value));
    }
  }

  @Override
  public @NotNull UUID getOwner() {
    return uuid;
  }

  @Override
  public String[] getWatched() {
    return settingMap.keySet().toArray(new String[0]);
  }
}
