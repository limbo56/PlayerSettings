package me.limbo56.playersettings.command;

import me.limbo56.playersettings.command.subcommand.*;
import me.limbo56.playersettings.utils.storage.CollectionStore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStore extends CollectionStore<CommandBase> implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 1) {
            new HelpCommand().executeCommand(commandSender, args);
            return false;
        }

        getCommand(args[0]).processCommand(commandSender, args);
        return true;
    }

    @Override
    public void register() {
        super.register();
        addToStore(new HelpCommand());
        addToStore(new OpenCommand());
        addToStore(new ReloadCommand());
        addToStore(new SetCommand());
        addToStore(new GetCommand());

        // Register plugin command
        Bukkit.getPluginCommand("settings").setExecutor(this);
    }

    private CommandBase getCommand(String name) {
        for (CommandBase commandBase : getStored()) {
            if (commandBase.getName().equalsIgnoreCase(name)) {
                return commandBase;
            }
        }

        return new HelpCommand();
    }
}
