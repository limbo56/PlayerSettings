package me.limbo56.playersettings.listener;

import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VisibilitySettingListener implements Listener {
  private final SettingsManager settingsManager;
  private final PluginConfiguration pluginConfiguration;
  private final UserManager userManager;

  public VisibilitySettingListener() {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.pluginConfiguration = plugin.getConfiguration();
    this.settingsManager = plugin.getSettingsManager();
    this.userManager = plugin.getUserManager();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!settingsManager.isRegistered(Settings.visibility().getName())) {
      return;
    }

    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    getObservers().forEach(settingOwner -> settingOwner.getPlayer().hidePlayer(player));
  }

  private Collection<SettingUser> getObservers() {
    return userManager.getUsersBySetting(Settings.visibility().getName(), false);
  }
}
