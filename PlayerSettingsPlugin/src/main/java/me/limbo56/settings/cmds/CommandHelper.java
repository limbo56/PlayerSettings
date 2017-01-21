package me.limbo56.settings.cmds;

import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:55 AM
 */
public class CommandHelper {

    private CommandSender sender;
    @SuppressWarnings("unused")
    private Command cmd;

    public CommandHelper(CommandSender sender, Command cmd) {
        this.sender = sender;
        this.cmd = cmd;
    }

    public void noPermission() {
        PlayerUtils.sendMessage(sender, Cache.NO_PERMISSIONS);
    }

    public void noConsole() {
        PlayerUtils.sendMessage(sender, Cache.CHAT_TITLE + ChatColor.RED + "This command can only be run in-game!");
    }

}