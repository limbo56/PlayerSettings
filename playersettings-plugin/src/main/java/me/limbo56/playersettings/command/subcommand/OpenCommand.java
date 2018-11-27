package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.command.CommandBase;
import org.bukkit.command.CommandSender;

public class OpenCommand extends CommandBase {
    public OpenCommand() {
        super(1, "open", "", "Opens the settings menu", null);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        // TODO: Implement command
    }
}
