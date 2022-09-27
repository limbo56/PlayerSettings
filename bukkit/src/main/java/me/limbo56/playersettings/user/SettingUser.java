package me.limbo56.playersettings.user;

import static me.limbo56.playersettings.settings.SettingValue.SETTING_VALUE;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import java.util.Collections;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SettingUser {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private final UUID uuid;
  private final SettingWatcher settingWatcher;
  private boolean loading;

  public SettingUser(UUID uuid, SettingWatcher settingWatcher) {
    this.uuid = uuid;
    this.settingWatcher = settingWatcher;
    this.loading = true;
  }

  public void loadSettings() {
    plugin.getDataManager().loadUsers(Collections.singleton(this));
  }

  public void saveSettings() {
    plugin.getDataManager().saveUsers(Collections.singleton(this));
  }

  public boolean hasSettingEnabled(String settingName) {
    return settingWatcher.getValue(settingName) > 0;
  }

  public boolean hasSettingPermissions(@NotNull String settingName) {
    Setting setting = plugin.getSettingsContainer().getSetting(settingName);
    return PlayerSettingsProvider.getSettingPermissionLevel(this.getPlayer(), setting) > 0;
  }

  public boolean canChangeSettingTo(Setting setting, int level) {
    Player player =
        Preconditions.checkNotNull(this.getPlayer(), "Player '" + uuid + "' is not online");

    // Check if the value was changed
    String settingName = setting.getName();
    if (level == settingWatcher.getValue(settingName)) {
      Text.fromMessages("commands.setting-unchanged")
          .placeholder("%setting%", settingName)
          .placeholder("%value%", SETTING_VALUE.format(level))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    // Allow change to default setting value
    if (level == setting.getDefaultValue()) {
      return true;
    }

    // Ignore players who don't have permission to change the setting
    if (!this.hasSettingPermissions(settingName)) {
      Text.fromMessages("settings.no-access")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    // Ignore values higher than the player's max permission level
    int playerMaxLevel = PlayerSettingsProvider.getSettingPermissionLevel(player, setting);
    if (!Range.closed(-playerMaxLevel, playerMaxLevel).contains(level)) {
      Text.fromMessages("settings.low-access-level")
          .placeholder("%setting%", settingName)
          .placeholder("%max%", String.valueOf(playerMaxLevel))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    // Ignore levels out of the setting's value range
    int settingMaxLevel = setting.getMaxValue();
    if (!Range.closed(-settingMaxLevel, settingMaxLevel).contains(level)) {
      Text.fromMessages("commands.setting-invalid-level")
          .placeholder("%max%", String.valueOf(settingMaxLevel))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    return true;
  }

  public boolean isLoading() {
    return loading;
  }

  public void setLoading(boolean loading) {
    this.loading = loading;
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
