package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSettings.CHAT_SETTING;
import static me.limbo56.playersettings.settings.DefaultSettings.FLY_SETTING;

import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatSettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    String chatSettingName = CHAT_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingLoaded(chatSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Remove players with chat disabled from recipients list
    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (user.hasSettingEnabled(chatSettingName)) {
      for (SettingUser settingUser : getPlayersWithChatDisabled()) {
        event.getRecipients().remove(settingUser.getPlayer());
      }
      return;
    }

    // Cancel chat event if player has chat disabled
    event.setCancelled(true);
    Text.fromMessages("chat.self-disabled")
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
  }

  private Collection<SettingUser> getPlayersWithChatDisabled() {
    return PLUGIN.getUserManager().getUsersWithSettingValue(CHAT_SETTING.getName(), false);
  }
}
