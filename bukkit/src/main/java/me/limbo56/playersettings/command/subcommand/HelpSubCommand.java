package me.limbo56.playersettings.command.subcommand;

import java.util.StringJoiner;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.util.Players;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpSubCommand extends SubCommand {
  private static final String HELP_HEADER = "&e------- &6Settings Help &e---------\n";
  private static final String COMMAND_FORMAT = "&f/settings %s%s &8: &7%s";

  public HelpSubCommand(PlayerSettings plugin) {
    super(plugin, "help", "Prints a list with available commands", "", 1, null);
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    String helpMessage = buildHelpMessage(sender);
    Players.sendMessage(sender, helpMessage);
  }

  private String buildHelpMessage(CommandSender sender) {
    StringJoiner message = new StringJoiner("\n");
    message.add(HELP_HEADER);
    for (String command : plugin.getCommandManager().getAllowedSubCommands(sender)) {
      message.add(buildCommandHelpMessage(command));
    }
    return message.toString();
  }

  private String buildCommandHelpMessage(String commandName) {
    SubCommand command = plugin.getCommandManager().getSubCommand(commandName);
    String usage = command.getUsage();
    usage = usage.isEmpty() ? "" : " " + usage;
    return String.format(COMMAND_FORMAT, command.getName(), usage, command.getDescription());
  }
}
