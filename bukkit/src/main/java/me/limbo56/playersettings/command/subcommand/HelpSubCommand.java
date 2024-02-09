package me.limbo56.playersettings.command.subcommand;

import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.CommandManager;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.message.MessageProvider;
import me.limbo56.playersettings.util.Players;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpSubCommand extends SubCommand {
  private final CommandManager commandManager;
  private final MessageProvider messageProvider;

  public HelpSubCommand(PlayerSettings plugin) {
    super(plugin, "help", "Prints a list with available commands", "", 1, null);
    this.commandManager = plugin.getCommandManager();
    this.messageProvider = plugin.getMessenger().getMessageProvider();
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    List<SubCommand> commands = commandManager.getAllowedSubCommands(sender);
    Players.sendMessage(sender, messageProvider.getCommandHelpMessage(player, commands));
  }
}
