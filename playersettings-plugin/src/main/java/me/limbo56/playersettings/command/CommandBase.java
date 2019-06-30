package me.limbo56.playersettings.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.limbo56.playersettings.command.subcommand.HelpCommand;
import me.limbo56.playersettings.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public abstract class CommandBase {
    private int arguments;
    private String name;
    private String usage;
    private String description;
    private String permission;

    protected abstract void executeCommand(CommandSender sender, String[] args);

    public void processCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command");
            return;
        }

        if (args.length < arguments) {
            new HelpCommand().executeCommand(sender, null);
            return;
        }

        if (permission != null) {
            if (!sender.hasPermission(permission)) {
                PlayerUtils.sendConfigMessage((Player) sender, "settings.noPermission");
                return;
            }
        }

        executeCommand(sender, args);
    }
}
