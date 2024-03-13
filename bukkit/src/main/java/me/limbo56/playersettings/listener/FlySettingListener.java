package me.limbo56.playersettings.listener;

import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlySettingListener implements Listener {
  private final PlayerSettings plugin;
  private final UserManager userManager;
  private final PluginConfiguration pluginConfiguration;
  private final SettingsConfiguration settingsConfiguration;

  public FlySettingListener() {
    this.plugin = PlayerSettings.getInstance();
    this.userManager = plugin.getUserManager();

    ConfigurationManager configurationManager = plugin.getConfigurationManager();
    this.pluginConfiguration = configurationManager.getConfiguration(PluginConfiguration.class);
    this.settingsConfiguration = configurationManager.getConfiguration(SettingsConfiguration.class);
  }

  @EventHandler
  public void onToggleFlight(PlayerToggleFlightEvent event) {
    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    String flySettingName = Settings.fly().getName();
    UUID uuid = player.getUniqueId();
    SettingUser user = userManager.getUser(uuid);
    if (!user.hasSettingPermissions(flySettingName) || !user.hasSettingEnabled(flySettingName)) {
      return;
    }

    user.setFlying(event.isFlying());
    if (settingsConfiguration.isSaveFlightStateEnabled()) {
      userManager.getSaveFlightStateDebounced().call(uuid);
    }
  }

  @EventHandler
  public void onGameModeChange(PlayerGameModeChangeEvent event) {
    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    String flySettingName = Settings.fly().getName();
    SettingUser user = userManager.getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(flySettingName)
        || !user.hasSettingEnabled(flySettingName) && isFlightGameMode(event.getNewGameMode())) {
      return;
    }

    new TaskChain().sync(map -> player.setAllowFlight(true)).runSyncLater(plugin, 0L);
  }

  private boolean isFlightGameMode(GameMode gameMode) {
    return gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR;
  }
}
