package me.limbo56.playersettings.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.util.Players;
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
  private final CommandManager commandManager;
  private final Messenger messenger;
  private final MessagesConfiguration messagesConfiguration;

  public SubCommandExecutor(PlayerSettings plugin) {
    this.plugin = plugin;
    this.commandManager = plugin.getCommandManager();
    this.messenger = plugin.getMessenger();
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
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

    if (!(sender instanceof Player)) {
      messenger.sendMessage(sender, "&cYou must be a player to execute this command");
      return false;
    }

    Player player = (Player) sender;
    Optional<SubCommand> optionalDefaultSubCommand = commandManager.getDefaultSubCommand();
    if (args.length < 1) {
      optionalDefaultSubCommand.ifPresent(defaultCommand -> defaultCommand.execute(player, args));
      return false;
    }

    Optional<SubCommand> optionalSubCommand = commandManager.getSubCommand(args[0]);
    if (!optionalSubCommand.isPresent()) {
      Stream.of(commandManager.getSubCommand("help"), optionalDefaultSubCommand)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst()
          .ifPresent(subCommand -> subCommand.execute(player, new String[0]));
      return false;
    }

    SubCommand subCommand = optionalSubCommand.get();
    if (args.length < subCommand.getArguments()) {
      Players.sendMessage(
          player, messenger.getMessageProvider().getCommandHelpMessage(player, subCommand));
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
    List<String> accessibleCommands = commandManager.getAllowedSubCommandNames(sender);
    if (args.length < 2) {
      final List<String> completions = new ArrayList<>();
      StringUtil.copyPartialMatches(args[0], accessibleCommands, completions);
      Collections.sort(completions);
      return completions;
    }

    Optional<SubCommand> optionalSubCommand = commandManager.getSubCommand(args[0]);
    if (!accessibleCommands.contains(args[0]) || !optionalSubCommand.isPresent()) {
      return new ArrayList<>();
    }

    return optionalSubCommand.get().onTabComplete(sender, args);
  }
}
