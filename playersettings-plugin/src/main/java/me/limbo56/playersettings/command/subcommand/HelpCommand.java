package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.command.CommandBase;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandBase {
    public HelpCommand() {
        super(1, "help", "", "Prints a list with available commands", null);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        // TODO: Implement command
    }
}
