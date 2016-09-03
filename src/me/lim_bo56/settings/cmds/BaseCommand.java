package me.lim_bo56.settings.cmds;

import me.lim_bo56.settings.Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:48 AM
 */
public abstract class BaseCommand {

    protected Core plugin;

    public BaseCommand(Core plugin) {
        this.plugin = plugin;
    }

    public abstract void executeCommand(CommandSender sender, Command cmd, String[] args);
}