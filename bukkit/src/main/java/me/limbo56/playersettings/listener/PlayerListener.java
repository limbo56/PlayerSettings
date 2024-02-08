package me.limbo56.playersettings.listener;

import java.util.Map;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {
  private final PlayerSettings plugin;
  private final UserManager userManager;
  private final SettingsManager settingsManager;
  private final PluginConfiguration pluginConfiguration;

  public PlayerListener(PlayerSettings plugin) {
    this.plugin = plugin;
    this.userManager = plugin.getUserManager();
    this.settingsManager = plugin.getSettingsManager();
    this.pluginConfiguration = plugin.getConfiguration();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Send update alert
    if (player.isOp() && pluginConfiguration.hasUpdateAlertsEnabled()) {
      Players.sendVersionMessage(player);
    }

    userManager.loadUser(player.getUniqueId());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    userManager.unloadUser(player.getUniqueId());
  }

  @EventHandler
  public void onChangeWorld(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    boolean isUserLoaded = userManager.isUserLoaded(uuid);
    boolean allowedWorld = pluginConfiguration.isAllowedWorld(player.getWorld().getName());

    if (allowedWorld && !isUserLoaded) {
      userManager.loadUser(uuid);
    } else if (!allowedWorld && isUserLoaded) {
      userManager.unloadUser(uuid);
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    new TaskChain().sync(new RespawnReapplyEffect(player.getUniqueId())).runSyncLater(plugin, 0);
  }

  private class RespawnReapplyEffect implements TaskChain.Task {
    private final UUID uuid;

    private RespawnReapplyEffect(UUID uuid) {
      this.uuid = uuid;
    }

    @Override
    public void accept(Map<String, Object> map) {
      SettingWatcher watcher = userManager.getSettingWatcher(uuid);
      for (Setting setting : settingsManager.getSettings()) {
        boolean hasRespawnTrigger = setting.hasTrigger("respawn");
        PluginLogger.debug(
            "Reloading setting `" + setting.getName() + "` on death `" + hasRespawnTrigger + "`");
        if (!hasRespawnTrigger) {
          continue;
        }

        String settingName = setting.getName();
        int value = watcher.getValue(settingName);
        watcher.setValue(settingName, value, false);
      }
    }
  }
}
