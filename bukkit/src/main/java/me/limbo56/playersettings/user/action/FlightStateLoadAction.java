package me.limbo56.playersettings.user.action;

import java.util.Optional;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.database.DataManager;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlightStateLoadAction implements UserAction {
  public static final String STATE_KEY = "flight-state";
  private final PlayerSettings plugin;
  private final SettingsManager settingsManager;
  private final DataManager dataManager;
  private final PluginConfiguration pluginConfiguration;
  private final SettingsConfiguration settingsConfiguration;

  public FlightStateLoadAction() {
    this.plugin = PlayerSettings.getInstance();
    this.settingsManager = plugin.getSettingsManager();
    this.dataManager = plugin.getDataManager();

    ConfigurationManager configurationManager = plugin.getConfigurationManager();
    this.pluginConfiguration = configurationManager.getConfiguration(PluginConfiguration.class);
    this.settingsConfiguration = configurationManager.getConfiguration(SettingsConfiguration.class);
  }

  @Override
  public void accept(SettingUser user) {
    String flySettingName = Settings.fly().getName();
    Player player = user.getPlayer();
    if (!(player != null
        && pluginConfiguration.isAllowedWorld(player.getWorld().getName())
        && settingsManager.isRegistered(flySettingName)
        && settingsConfiguration.isSettingEnabled(flySettingName)
        && user.hasSettingPermissions(flySettingName)
        && user.hasSettingEnabled(flySettingName))) {
      return;
    }

    // Load saved flight state if enabled in the configuration
    PluginLogger.debug(ChatColor.WHITE + "Loading flight state of '" + player.getUniqueId() + "'");
    Setting setting = settingsManager.getSetting(flySettingName);
    new TaskChain()
        .loadAsync(STATE_KEY, () -> getFlightState(player, setting))
        .sync(createLoadTask(user))
        .runAsync(plugin);
  }

  @NotNull
  private TaskChain.Task createLoadTask(SettingUser user) {
    return data -> {
      Player player = user.getPlayer();
      if (!player.isOnline()) {
        return;
      }

      if (settingsConfiguration.isSaveFlightStateEnabled()
          && settingsConfiguration.isForceFlightOnJoinEnabled()) {
        boolean hasFlightEnabled = (boolean) data.get(STATE_KEY);
        PluginLogger.debug("Has flight enabled: " + hasFlightEnabled);
        if (hasFlightEnabled) {
          player.setAllowFlight(true);
        }
        player.setFlying(hasFlightEnabled);
        user.setFlying(hasFlightEnabled);
      } else if (settingsConfiguration.isForceFlightOnJoinEnabled()) {
        player.setAllowFlight(true);
        player.setFlying(true);
        user.setFlying(true);
      }
    };
  }

  private boolean getFlightState(Player player, Setting setting) {
    Integer flightState =
        Optional.ofNullable(dataManager.getExtra(player.getUniqueId(), setting, STATE_KEY))
            .map(Integer::parseInt)
            .orElse(0);
    return settingsConfiguration.isSaveFlightStateEnabled() && flightState.equals(1);
  }

  @Override
  public boolean shouldExecute() {
    return settingsManager.isRegistered(Settings.fly().getName());
  }
}
