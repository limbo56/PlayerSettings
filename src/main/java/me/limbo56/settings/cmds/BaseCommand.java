package me.limbo56.settings.cmds;

import me.limbo56.settings.PlayerSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:48 AM
 */
public abstract class BaseCommand {

    protected PlayerSettings plugin;

    public BaseCommand(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    public abstract void executeCommand(CommandSender sender, Command cmd, String[] args);
}