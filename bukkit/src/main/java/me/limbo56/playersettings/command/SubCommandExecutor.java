package me.limbo56.playersettings.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.subcommand.HelpSubCommand;
import me.limbo56.playersettings.util.Text;
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
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

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
    if (args.length < 1) {
      commandManager.getCommand("help").execute(sender, args);
      return false;
    }

    SubCommand subCommand = commandManager.getCommand(args[0]);
    if (!(sender instanceof Player)) {
      Text.from("&cYou must be a player to execute this command")
          .sendMessage(sender, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    if (args.length < subCommand.getArguments()) {
      new HelpSubCommand().execute(sender, new String[0]);
      return false;
    }

    String permission = subCommand.getPermission();
    if (permission != null && !sender.hasPermission(permission)) {
      Text.fromMessages("commands.no-access")
          .sendMessage(sender, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    subCommand.execute(sender, args);
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
    List<String> accessibleCommands = commandManager.getAccessibleCommands(sender);
    if (args.length < 2) {
      final List<String> completions = new ArrayList<>();
      StringUtil.copyPartialMatches(args[0], accessibleCommands, completions);
      Collections.sort(completions);
      return completions;
    }

    if (!accessibleCommands.contains(args[0])) {
      return new ArrayList<>();
    }

    return commandManager.getCommand(args[0]).onTabComplete(sender, args);
  }
}
