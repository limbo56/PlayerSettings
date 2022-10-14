package me.limbo56.playersettings.user;

import static me.limbo56.playersettings.PlayerSettingsProvider.getPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.settings.SettingUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SimpleSettingWatcher implements SettingWatcher {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private final UUID uuid;
  private final Map<String, Integer> valueMap;

  public SimpleSettingWatcher(UUID owner) {
    this.uuid = owner;
    this.valueMap = new HashMap<>();
  }

  @Override
  public int getValue(@NotNull String settingName) {
    int defaultValue = plugin.getSettingsContainer().getSetting(settingName).getDefaultValue();
    return valueMap.getOrDefault(settingName, defaultValue);
  }

  @Override
  public void setValue(@NotNull String settingName, int value, boolean silent) {
    Integer previousValue = Optional.ofNullable(valueMap.put(settingName, value)).orElse(0);
    if (!silent) {
      Setting setting = plugin.getSettingsContainer().getSetting(settingName);
      Player player = Bukkit.getPlayer(getOwner());
      new SettingUpdateTask(setting, player, previousValue, value).runTask(getPlugin());
    }
  }

  @Override
  public @NotNull UUID getOwner() {
    return uuid;
  }

  @Override
  public String[] getWatched() {
    return valueMap.keySet().toArray(new String[0]);
  }
}
