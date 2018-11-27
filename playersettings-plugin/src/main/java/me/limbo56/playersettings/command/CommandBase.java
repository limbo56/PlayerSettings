package me.limbo56.playersettings.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandBase {
    private int arguments;
    private String name;
    private String usage;
    private String description;
    private String permission;

    public CommandBase(int arguments, String name, String usage, String description, String permission) {
        this.arguments = arguments;
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.permission = permission;
    }

    protected abstract void executeCommand(CommandSender sender, String[] args);

    public final void processCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        if (args.length != arguments) {
            return;
        }

        if (permission != null) {
            if (!sender.hasPermission(permission)) {
                return;
            }
        }

        executeCommand(sender, args);
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }
}
