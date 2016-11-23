package me.lim_bo56.settings.cmds;

import me.lim_bo56.settings.utils.PlayerUtils;
import me.lim_bo56.settings.utils.Variables;
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
    private Command cmd;

    public CommandHelper(CommandSender sender, Command cmd) {
        this.sender = sender;
        this.cmd = cmd;
    }

    public void noPermission() {
        PlayerUtils.sendMessage(sender, Variables.NO_PERMISSIONS);
    }

    public void noConsole() {
        PlayerUtils.sendMessage(sender, Variables.CHAT_TITLE + ChatColor.RED + "This command can only be run in-game!");
    }

}