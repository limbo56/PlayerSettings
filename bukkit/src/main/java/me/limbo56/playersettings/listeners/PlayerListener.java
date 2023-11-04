package me.limbo56.playersettings.listeners;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.PluginUpdater;
import me.limbo56.playersettings.util.TaskChain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

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
    loadPlayerSettings(player);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    savePlayerSettings(player);
  }

  @EventHandler
  public void onChangeWorld(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    String worldName = player.getWorld().getName();
    boolean userLoaded = PLUGIN.getUserManager().isUserLoaded(uuid);
    boolean allowedWorld = PLUGIN.getPluginConfiguration().isAllowedWorld(worldName);

    if (allowedWorld && !userLoaded) {
      loadPlayerSettings(player);
    } else if (!allowedWorld && userLoaded) {
      savePlayerSettings(player);
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    new TaskChain().sync(reapplySettingEffects(player)).runSyncLater(0);
  }

  @NotNull
  private Consumer<Map<String, Object>> reapplySettingEffects(Player player) {
    return data -> {
      SettingWatcher watcher = PLUGIN.getUserManager().getSettingWatcher(player.getUniqueId());
      for (Setting setting : PLUGIN.getSettingsManager().getSettingMap().values()) {
        boolean hasRespawnTrigger = PLUGIN.getSettingsManager().hasTriggers(setting, "respawn");
        PluginLogger.debug(
            "Reloading setting `" + setting.getName() + "` on death `" + hasRespawnTrigger + "`");
        if (!hasRespawnTrigger) {
          continue;
        }

        String settingName = setting.getName();
        int value = watcher.getValue(settingName);
        watcher.setValue(settingName, value, false);
      }
    };
  }

  private void loadPlayerSettings(Player player) {
    UUID uuid = player.getUniqueId();
    new TaskChain().async(data -> PLUGIN.getUserManager().loadUser(uuid)).runAsync();
  }

  private void savePlayerSettings(Player player) {
    UUID uuid = player.getUniqueId();
    SettingUser user = PLUGIN.getUserManager().getUser(uuid);
    user.clearSettingEffects();

    // Save and unload user
    new TaskChain().async(saveAndUnload(uuid)).runAsync();
  }

  @NotNull
  private Consumer<Map<String, Object>> saveAndUnload(UUID uuid) {
    return data -> {
      PLUGIN.getSettingsMenuManager().unload(uuid);
      PLUGIN.getUserManager().saveUser(uuid);
      PLUGIN.getUserManager().unloadUser(uuid);
    };
  }
}
