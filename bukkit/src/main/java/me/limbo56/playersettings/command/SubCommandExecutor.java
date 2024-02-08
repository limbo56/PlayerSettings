package me.limbo56.playersettings.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command executor for the <code>/settings</code> command.
 *
 * <p>This class is responsible for executing the sub commands of the main command.
 */
public class SubCommandExecutor implements org.bukkit.command.CommandExecutor, TabCompleter {
  private final PlayerSettings plugin;
  private final MessagesConfiguration messagesConfiguration;
  private final Messenger messenger;

  public SubCommandExecutor(PlayerSettings plugin) {
    this.plugin = plugin;
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
    this.messenger = plugin.getMessenger();
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      String[] args) {
    if (plugin.isReloading()) {
      return false;
    }

    CommandManager commandManager = plugin.getCommandManager();
    SubCommand help = commandManager.getSubCommand("help");
    if (args.length < 1) {
      help.execute(sender, args);
      return false;
    }

    SubCommand subCommand = commandManager.getSubCommand(args[0]);
    if (!(sender instanceof Player)) {
      messenger.sendMessage(sender, "&cYou must be a player to execute this command");
      return false;
    }

    Player player = (Player) sender;
    if (args.length < subCommand.getArguments()) {
      help.execute(player, new String[0]);
      return false;
    }

    String permission = subCommand.getPermission();
    if (permission != null && !player.hasPermission(permission)) {
      messenger.sendMessage(player, messagesConfiguration.getMessage("commands.no-access"));
      return false;
    }

    subCommand.execute(player, args);
    return true;
  }

  @Nullable
  @Override
  public List<String> onTabComplete(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {
    CommandManager commandManager = plugin.getCommandManager();
    List<String> accessibleCommands = commandManager.getAllowedSubCommands(sender);
    if (args.length < 2) {
      final List<String> completions = new ArrayList<>();
      StringUtil.copyPartialMatches(args[0], accessibleCommands, completions);
      Collections.sort(completions);
      return completions;
    }

    if (!accessibleCommands.contains(args[0])) {
      return new ArrayList<>();
    }

    return commandManager.getSubCommand(args[0]).onTabComplete(sender, args);
  }
}
