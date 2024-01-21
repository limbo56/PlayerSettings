package me.limbo56.playersettings.command.subcommand;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.util.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpSubCommand extends SubCommand {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public HelpSubCommand() {
    super("help", "Prints a list with available commands", "", 1, null);
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Text.from(buildHelpMessage(sender)).sendMessage(sender);
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }

  private String buildHelpMessage(CommandSender senders) {
    StringJoiner message = new StringJoiner("\n", "&e------- &6Settings Help &e---------\n", "");
    for (String command : PLUGIN.getCommandManager().getAccessibleCommands(senders)) {
      message.add(buildCommandHelpMessage(command));
    }
    return message.toString();
  }

  private String buildCommandHelpMessage(String commandName) {
    SubCommand command = PLUGIN.getCommandManager().getCommand(commandName);
    String usage = command.getUsage();
    usage = usage.isEmpty() ? "" : " " + usage;
    return String.format(
        "&f/settings %s%s &8: &7%s", command.getName(), usage, command.getDescription());
  }
}
