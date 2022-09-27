package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSetting.CHAT_SETTING;

import java.util.Collection;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatSettingListener implements Listener {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Remove players with chat disabled from recipients list
    SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
    String chatSettingName = CHAT_SETTING.getSetting().getName();
    if (user.hasSettingEnabled(chatSettingName)) {
      getPlayersWithChatDisabled().forEach(event.getRecipients()::remove);
      return;
    }

    // Cancel chat event if player has chat disabled
    event.setCancelled(true);
    Text.fromMessages("chat.self-disabled")
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
  }

  private Collection<Player> getPlayersWithChatDisabled() {
    String chatSettingName = CHAT_SETTING.getSetting().getName();
    return plugin.getUserManager().getUsers().stream()
        .filter(user -> !user.hasSettingEnabled(chatSettingName))
        .map(SettingUser::getPlayer)
        .collect(Collectors.toList());
  }
}
