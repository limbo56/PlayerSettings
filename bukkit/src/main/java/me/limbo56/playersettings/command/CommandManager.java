package me.limbo56.playersettings.command;

import me.limbo56.playersettings.command.subcommand.HelpSubCommand;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Class that manages all commands for the plugin. */
public class CommandManager {
  private final Map<String, SubCommand> subCommands = new HashMap<>();

  /**
   * Adds a subcommand to the command manager.
   *
   * @param command Subcommand to add.
   */
  public void registerSubCommand(SubCommand command) {
    subCommands.put(command.getName(), command);
  }

  public void unloadAll() {
    subCommands.clear();
  }

  public List<String> getAccessibleCommands(CommandSender sender) {
    return subCommands.values().stream()
        .filter(
            subCommand -> {
              String permission = subCommand.getPermission();
              return permission == null || sender.hasPermission(permission);
            })
        .map(SubCommand::getName)
        .collect(Collectors.toList());
  }

  /**
   * Gets a subcommand from the command manager.
   *
   * @param name Name of the subcommand.
   * @return Subcommand if found, otherwise null.
   */
  public SubCommand getCommand(String name) {
    return Optional.ofNullable(subCommands.get(name)).orElse(new HelpSubCommand());
  }

  /**
   * Gets all registered subcommands.
   *
   * @return All registered subcommands.
   */
  public Map<String, SubCommand> getSubCommandsMap() {
    return subCommands;
  }
}
