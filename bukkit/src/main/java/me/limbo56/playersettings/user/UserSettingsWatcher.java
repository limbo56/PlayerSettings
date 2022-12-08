package me.limbo56.playersettings.user;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.settings.SettingUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserSettingsWatcher implements SettingWatcher {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final UUID uuid;
  private final Map<String, Integer> settingMap;

  public UserSettingsWatcher(UUID owner) {
    this.uuid = owner;
    this.settingMap = new HashMap<>();
  }

  @Override
  public int getValue(@NotNull String settingName) {
    int defaultValue = PLUGIN.getSettingsManager().getSetting(settingName).getDefaultValue();
    return settingMap.getOrDefault(settingName, defaultValue);
  }

  @Override
  public void setValue(@NotNull String settingName, int value, boolean silent) {
    Integer previousValue = Optional.ofNullable(settingMap.put(settingName, value)).orElse(0);
    if (!silent) {
      Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
      Player player = Bukkit.getPlayer(getOwner());
      new SettingUpdateTask(setting, player, previousValue, value).runTask(PLUGIN);
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
