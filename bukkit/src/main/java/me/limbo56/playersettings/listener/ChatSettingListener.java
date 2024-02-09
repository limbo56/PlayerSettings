package me.limbo56.playersettings.listener;

import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatSettingListener implements Listener {
  private final UserManager userManager;
  private final SettingsManager settingsManager;
  private final Messenger messenger;
  private final PluginConfiguration pluginConfiguration;

  public ChatSettingListener() {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.userManager = plugin.getUserManager();
    this.settingsManager = plugin.getSettingsManager();
    this.messenger = plugin.getMessenger();
    this.pluginConfiguration = plugin.getConfiguration();
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    String chatSettingName = Settings.chat().getName();
    if (!settingsManager.isRegistered(chatSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Remove players with chat disabled from recipients list
    SettingUser user = userManager.getUser(player.getUniqueId());
    if (user.hasSettingEnabled(chatSettingName)) {
      for (SettingUser recipient : getDisabledRecipients()) {
        event.getRecipients().remove(recipient.getPlayer());
      }
      return;
    }

    // Cancel chat event if player has chat disabled
    Setting setting = settingsManager.getSetting(chatSettingName);
    event.setCancelled(true);
    messenger.sendMessage(
        player, messenger.getMessageProvider().getSettingDisabledMessage(player, setting));
  }

  private Collection<SettingUser> getDisabledRecipients() {
    return userManager.getUsersBySetting(Settings.chat().getName(), false);
  }
}
