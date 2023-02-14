package me.limbo56.playersettings.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/** A sub command of the <code>/settings</code> command. */
public abstract class SubCommand {
  private final String name;
  private final String description;
  private final String usage;
  private final int arguments;
  private final String permission;

  protected SubCommand(
      String name, String description, String usage, int arguments, String permission) {
    this.name = name;
    this.description = description;
    this.usage = usage;
    this.arguments = arguments;
    this.permission = permission;
  }

  /**
   * Executes the command for the given sender and arguments.
   *
   * @param sender Sender of the command.
   * @param args Arguments of the command.
   */
  protected abstract void execute(@NotNull CommandSender sender, @NotNull String[] args);

  public abstract List<String> onTabComplete(CommandSender sender, String[] args);

  /**
   * Gets the name of the command.
   *
   * @return Name of the command.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the usage description of the command.
   *
   * @return Usage of the command.
   */
  public String getUsage() {
    return usage;
  }

  /**
   * Gets the description that will be shown in the help command.
   *
   * @return Command description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the number of arguments the command requires.
   *
   * @return Number of arguments the command requires.
   */
  public int getArguments() {
    return arguments;
  }

  /**
   * Gets the permission required to execute the command
   *
   * @return Permission to execute command or null if no permission is required.
   */
  public String getPermission() {
    return permission;
  }
}
