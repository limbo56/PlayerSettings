package me.limbo56.playersettings.command;

import java.util.*;
import me.limbo56.playersettings.command.subcommand.HelpSubCommand;
import org.bukkit.command.CommandSender;

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

  /**
   * Gets a subcommand from the command manager.
   *
   * @param name Name of the subcommand.
   * @return Subcommand if found, otherwise null.
   */
  public SubCommand getCommand(String name) {
    return Optional.ofNullable(subCommands.get(name)).orElse(new HelpSubCommand());
  }

  public void unloadAll() {
    subCommands.clear();
  }

  public List<String> getAccessibleCommands(CommandSender sender) {
    List<String> accessibleCommands = new ArrayList<>();

    for (SubCommand command : subCommands.values()) {
      String permission = command.getPermission();
      if (permission == null || sender.hasPermission(permission)) {
        String name = command.getName();
        accessibleCommands.add(name);
      }
    }

    return accessibleCommands;
  }
}
