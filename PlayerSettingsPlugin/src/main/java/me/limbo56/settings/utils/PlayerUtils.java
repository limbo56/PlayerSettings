package me.limbo56.settings.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:27:30 AM
 */
public class PlayerUtils {

    /**
     * Method to send a message to a command sender.
     *
     * @param sender  Command sender
     * @param message The message
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.isOnline()) {
                player.sendMessage(message);
            }
        } else {
            sender.sendMessage(message);
        }
    }

}
