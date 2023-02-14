package me.limbo56.playersettings.listeners;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.PluginUpdater;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class PlayerListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    if (player.isOp() && PLUGIN.getPluginConfiguration().hasUpdateAlertsEnabled()) {
      PluginUpdater.sendUpdateMessage(player);
    }

    // Load player
    loadPlayer(player);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    unloadPlayer(player);
  }

  @EventHandler
  public void onChangeWorld(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    UUID uniqueId = player.getUniqueId();
    String worldName = player.getWorld().getName();
    boolean userLoaded = PLUGIN.getUserManager().isUserLoaded(uniqueId);
    boolean allowedWorld = PLUGIN.getPluginConfiguration().isAllowedWorld(worldName);
    if (allowedWorld && !userLoaded) {
      loadPlayer(player);
    } else if (!allowedWorld && userLoaded) {
      unloadPlayer(player);
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    new TaskChain()
        .sync(
            data -> {
              SettingWatcher watcher =
                  PLUGIN.getUserManager().getSettingWatcher(player.getUniqueId());
              for (Setting setting : PLUGIN.getSettingsManager().getSettingMap().values()) {
                boolean hasRespawnTrigger =
                    PLUGIN.getSettingsManager().hasTriggers(setting, "respawn");
                PLUGIN
                    .getLogger()
                    .config(
                        "Reloading setting `"
                            + setting.getName()
                            + "` on death `"
                            + hasRespawnTrigger
                            + "`");
                if (!hasRespawnTrigger) {
                  continue;
                }

                String settingName = setting.getName();
                int value = watcher.getValue(settingName);
                watcher.setValue(settingName, value, false);
              }
            })
        .runSyncLater(0);
  }

  private void loadPlayer(Player player) {
    PLUGIN.getLogger().fine("Loading settings of player '" + player.getName() + "'");
    new TaskChain()
        .async(data -> PLUGIN.getUserManager().loadUser(player.getUniqueId()))
        .runAsync();
  }

  private void unloadPlayer(Player player) {
    UUID uuid = player.getUniqueId();
    SettingUser user = PLUGIN.getUserManager().getUser(uuid);
    user.clearSettingEffects();

    // Save and unload user
    PLUGIN.getLogger().fine("Saving settings of player '" + player.getName() + "'");
    new TaskChain()
        .async(
            data -> {
              PLUGIN.getSettingsMenuManager().unload(uuid);
              PLUGIN.getUserManager().saveUser(uuid);
              PLUGIN.getUserManager().unloadUser(uuid);
            })
        .runAsync();
  }
}
