package me.limbo56.playersettings.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.limbo56.playersettings.PlayerSettings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageReceiveListener implements PluginMessageListener {
    private PlayerSettings plugin;

    public MessageReceiveListener(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (subChannel.contains("Connect")) {
            plugin.getsPlayerStore().removeFromStore(player.getUniqueId());
        }
    }
}
