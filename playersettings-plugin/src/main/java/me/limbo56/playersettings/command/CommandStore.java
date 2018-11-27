package me.limbo56.playersettings.command;

import com.google.common.base.Preconditions;
import me.limbo56.playersettings.command.subcommand.HelpCommand;
import me.limbo56.playersettings.command.subcommand.OpenCommand;
import me.limbo56.playersettings.utils.storage.CollectionStore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.HashSet;

public class CommandStore implements CommandExecutor, CollectionStore<CommandBase> {
    private Collection<CommandBase> commands;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Preconditions.checkNotNull(commandSender);
        Preconditions.checkNotNull(command);

        if (!(args.length > 1)) {
            return false;
        }

        getCommand(args[1]).processCommand(commandSender, args);
        return true;
    }

    @Override
    public void register() {
        commands = new HashSet<>();
        addToStore(new HelpCommand());
        addToStore(new OpenCommand());

        // Register plugin command
        Bukkit.getPluginCommand("settings").setExecutor(this);
    }

    @Override
    public void unregister() {
        commands.clear();
    }

    @Override
    public void addToStore(CommandBase commandBase) {
        commands.add(commandBase);
    }

    @Override
    public Collection<CommandBase> getStored() {
        return commands;
    }

    private CommandBase getCommand(String name) {
        for (CommandBase commandBase : commands) {
            if (commandBase.getName().equalsIgnoreCase(name)) {
                return commandBase;
            }
        }
        return new HelpCommand();
    }
}
