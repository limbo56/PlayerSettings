package me.limbo56.playersettings.command.subcommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.util.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpSubCommand extends SubCommand {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

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
    return plugin.getCommandManager().getAccessibleCommands(senders).stream()
        .map(this::buildCommandHelpMessage)
        .collect(Collectors.joining("\n", "&e------- &6Settings Help &e---------\n", ""));
  }

  private String buildCommandHelpMessage(String commandName) {
    SubCommand command = plugin.getCommandManager().getCommand(commandName);
    String helpFormat = "&f/settings %s%s &8: &7%s";
    String name = command.getName();
    String usage = command.getUsage().equals("") ? "" : " " + command.getUsage();
    String description = command.getDescription();
    return String.format(helpFormat, name, usage, description);
  }
}
