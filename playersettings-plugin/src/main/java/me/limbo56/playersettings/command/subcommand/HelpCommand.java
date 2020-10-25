package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.CommandBase;
import me.limbo56.playersettings.command.CommandStore;
import me.limbo56.playersettings.utils.ColorUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandBase {

    public HelpCommand() {
        super(1, "help", "", "Prints a list with available commands", null);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append(ColorUtils.translateString("&eAvailable Commands: \n"));

        CommandStore commandStore = PlayerSettings.getPlugin().getCommandStore();
        for (CommandBase commandBase : commandStore.getStored()) {
            String helpFormat = ColorUtils.translateString("&e- /settings %s%s &7: &6%s \n");
            String name = commandBase.getName();
            String usage = commandBase.getUsage().equals("") ? "" : " " + commandBase.getUsage();
            String description = commandBase.getDescription();

            // Append message
            helpMessage.append(String.format(helpFormat, name, usage, description));
        }

        sender.sendMessage(helpMessage.toString());
    }
}
