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
  private final PluginConfiguration pluginConfiguration;
  private final SubCommand defaultCommand;
  private final Map<String, SubCommand> subCommands = new HashMap<>();

  public CommandManager(PlayerSettings plugin) {
    this(plugin, new DefaultSubCommand(plugin));
  }

  public CommandManager(PlayerSettings plugin, SubCommand defaultCommand) {
    this.plugin = plugin;
    this.pluginConfiguration = plugin.getConfiguration();
    this.defaultCommand = defaultCommand;
  }

  public void registerDefaultCommands() {
    SubCommandExecutor executor = new SubCommandExecutor(plugin);
    PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand("settings"));
    pluginCommand.setExecutor(executor);
    pluginCommand.setTabCompleter(executor);

    registerSubCommand(new OpenSubCommand(plugin));
    registerSubCommand(new HelpSubCommand(plugin));
    registerSubCommand(new ReloadSubCommand(plugin));
    registerSubCommand(new SetSubCommand(plugin));
    registerSubCommand(new GetSubCommand(plugin));
  }

  private void registerSubCommand(SubCommand subCommand) {
    String commandName = subCommand.getName();
    if (pluginConfiguration.isCommandEnabled(commandName)) {
      subCommands.put(commandName, subCommand);
    }
  }

  public void unloadAll() {
    subCommands.clear();
  }

  public void reloadCommands() {
    unloadAll();
    registerDefaultCommands();
  }

  public Optional<SubCommand> getDefaultSubCommand() {
    String defaultCommandName = pluginConfiguration.getDefaultCommand().toLowerCase();
    if (subCommands.containsKey(defaultCommandName)) {
      if ("open".equals(defaultCommandName) || "help".equals(defaultCommandName)) {
        return getSubCommand(defaultCommandName);
      }
    }
    return Optional.of(defaultCommand);
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
