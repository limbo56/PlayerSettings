package me.limbo56.playersettings.setting.callback;

import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.Players;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VisibilitySettingCallback implements SettingCallback {
  private final SettingsManager settingsManager;
  private final UserManager userManager;

  public VisibilitySettingCallback() {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.settingsManager = plugin.getSettingsManager();
    this.userManager = plugin.getUserManager();
  }

  @Override
  public void notifyChange(Player player, Setting setting, int value) {
    if (value > 0) {
      this.clear(player);
    } else {
      getObservers().forEach(player::hidePlayer);
    }
  }

  @Override
  public void clear(Player player) {
    getObservers().forEach(player::showPlayer);
  }

  @NotNull
  private Collection<Player> getObservers() {
    String vanishSettingName = Settings.vanish().getName();
    Collection<SettingUser> users =
        settingsManager.isRegistered(vanishSettingName)
            ? userManager.getUsersBySetting(vanishSettingName, false)
            : userManager.getUsers();
    return Players.filterOnlineUsers(users);
  }
}
