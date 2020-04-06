package me.limbo56.playersettings.listeners;

import lombok.AllArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static me.limbo56.playersettings.utils.PlayerUtils.filterPlayers;
import static me.limbo56.playersettings.utils.PlayerUtils.isAllowedWorld;

@AllArgsConstructor
public class ChatSettingListener implements Listener {
    private PlayerSettings plugin;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        SPlayer sPlayer = plugin.getSPlayer(player.getUniqueId());
        boolean hasChatEnabled = sPlayer.getSettingWatcher().getValue(plugin.getSetting("chat_setting")) == 1;

        if (!isAllowedWorld(player.getWorld().getName())) return;
        if (hasChatEnabled) {
            for (SPlayer chatDisabled : filterPlayers(this::filterByChatDisabled)) {
                event.getRecipients().remove(chatDisabled.getPlayer());
            }
            return;
        }

        event.setCancelled(true);
        PlayerUtils.sendConfigMessage(player, "settings.chatDisabled");
    }

    private boolean filterByChatDisabled(SPlayer sPlayer) {
        return sPlayer.getSettingWatcher().getValue(plugin.getSetting("chat_setting")) != 1;
    }
}
