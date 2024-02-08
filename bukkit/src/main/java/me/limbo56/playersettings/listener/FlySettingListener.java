package me.limbo56.playersettings.listener;

import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.database.DataManager;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.user.action.FlightStateLoadAction;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlySettingListener implements Listener {
  private final PlayerSettings plugin;
  private final UserManager userManager;
  private final SettingsManager settingsManager;
  private final DataManager dataManager;
  private final PluginConfiguration pluginConfiguration;
  private final SettingsConfiguration settingsConfiguration;

  public FlySettingListener() {
    this.plugin = PlayerSettings.getInstance();
    this.userManager = plugin.getUserManager();
    this.settingsManager = plugin.getSettingsManager();
    this.dataManager = plugin.getDataManager();

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
    if (!settingsManager.isRegistered(flySettingName)) {
      return;
    }

    SettingUser user = userManager.getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(flySettingName)) {
      return;
    }

    user.setFlying(event.isFlying());
  }

  @EventHandler
  public void onGameModeChange(PlayerGameModeChangeEvent event) {
    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    String flySettingName = Settings.fly().getName();
    if (!settingsManager.isRegistered(flySettingName)) {
      return;
    }

    SettingUser user = userManager.getUser(player.getUniqueId());
    if (!user.hasSettingPermissions(flySettingName)) {
      return;
    }

    player.setAllowFlight(true);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    String flySettingName = Settings.fly().getName();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())
        || !settingsManager.isRegistered(flySettingName)
        || !settingsConfiguration.isSaveFlightStateEnabled()) {
      return;
    }

    UUID uuid = player.getUniqueId();
    SettingUser user = userManager.getUser(uuid);
    if (!user.hasSettingPermissions(flySettingName) || !user.hasSettingEnabled(flySettingName)) {
      return;
    }

    Setting setting = settingsManager.getSetting(flySettingName);
    String value = String.valueOf(user.isFlying() ? 1 : 0);
    PluginLogger.debug(
        "Saving flight state '" + value + "' of player '" + player.getUniqueId() + "'");
    new TaskChain()
        .async(data -> dataManager.putExtra(uuid, setting, FlightStateLoadAction.STATE_KEY, value))
        .runAsync(plugin);
  }
}
