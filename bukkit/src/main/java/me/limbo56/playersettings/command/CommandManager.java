package me.limbo56.playersettings.command;

import java.util.*;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.subcommand.*;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

/** Class that manages all commands for the plugin. */
public class CommandManager {
  private final PlayerSettings plugin;
  private final Map<String, SubCommand> subCommands = new HashMap<>();
  private final PluginConfiguration pluginConfiguration;

  public CommandManager(PlayerSettings plugin) {
    this.plugin = plugin;
    this.pluginConfiguration = plugin.getConfiguration();
  }

  public void registerDefaultCommands() {
    SubCommandExecutor executor = new SubCommandExecutor(plugin);
    PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand("settings"));
    pluginCommand.setExecutor(executor);
    pluginCommand.setTabCompleter(executor);

    registerSubCommand(new OpenSubCommand(plugin), true);
    registerSubCommand(new HelpSubCommand(plugin));
    registerSubCommand(new ReloadSubCommand(plugin));
    registerSubCommand(new SetSubCommand(plugin));
    registerSubCommand(new GetSubCommand(plugin));
  }

  private void registerSubCommand(SubCommand subCommand) {
    registerSubCommand(subCommand, false);
  }

  private void registerSubCommand(SubCommand command, boolean force) {
    String commandName = command.getName();
    if (force || pluginConfiguration.isCommandEnabled(commandName)) {
      subCommands.put(commandName, command);
    }
  }

  public void unloadAll() {
    subCommands.clear();
  }

  public void reloadCommands() {
    unloadAll();
    registerDefaultCommands();
  }

  public Optional<SubCommand> getDefaultCommand() {
    String defaultCommandName = pluginConfiguration.getDefaultCommand().toLowerCase();
    if ("open".equals(defaultCommandName) || "help".equals(defaultCommandName)) {
      return getSubCommand(defaultCommandName);
    } else {
      return getSubCommand("open");
    }
  }

  public Optional<SubCommand> getSubCommand(String name) {
    return Optional.ofNullable(subCommands.get(name));
  }

  @NotNull
  public List<SubCommand> getAllowedSubCommands(CommandSender sender) {
    return subCommands.values().stream()
        .filter(subCommand -> isAllowedSubCommand(sender, subCommand))
        .collect(Collectors.toList());
  }

  @NotNull
  public List<String> getAllowedSubCommandNames(@NotNull CommandSender sender) {
    return subCommands.values().stream()
        .filter(subCommand -> isAllowedSubCommand(sender, subCommand))
        .map(SubCommand::getName)
        .collect(Collectors.toList());
  }

  private static boolean isAllowedSubCommand(CommandSender sender, SubCommand subCommand) {
    String permission = subCommand.getPermission();
    return permission == null || sender.hasPermission(permission);
  }
}
