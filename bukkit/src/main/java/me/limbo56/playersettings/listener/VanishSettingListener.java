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

public class VanishSettingListener implements Listener {
  private final SettingsManager settingsManager;
  private final PluginConfiguration pluginConfiguration;
  private final UserManager userManager;

  public VanishSettingListener() {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.settingsManager = plugin.getSettingsManager();
    this.pluginConfiguration = plugin.getConfiguration();
    this.userManager = plugin.getUserManager();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    String vanishSettingName = Settings.vanish().getName();
    if (!settingsManager.isRegistered(vanishSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    getObservers().forEach(observer -> player.hidePlayer(observer.getPlayer()));
  }

  private Collection<SettingUser> getObservers() {
    return userManager.getUsersBySetting(Settings.vanish().getName(), true);
  }
}
