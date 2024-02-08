package me.limbo56.playersettings.command;

import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.subcommand.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

/** Class that manages all commands for the plugin. */
public class CommandManager {
  private final PlayerSettings plugin;
  private final Map<String, SubCommand> subCommands = new HashMap<>();

  public CommandManager(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  public void registerDefaultCommands() {
    SubCommandExecutor executor = new SubCommandExecutor(plugin);
    PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand("settings"));
    pluginCommand.setExecutor(executor);
    pluginCommand.setTabCompleter(executor);

    registerSubCommand(new HelpSubCommand(plugin));
    registerSubCommand(new OpenSubCommand(plugin));
    registerSubCommand(new ReloadSubCommand(plugin));
    registerSubCommand(new SetSubCommand(plugin));
    registerSubCommand(new GetSubCommand(plugin));
  }

  private void registerSubCommand(SubCommand command) {
    subCommands.put(command.getName(), command);
  }

  public void unloadAll() {
    subCommands.clear();
  }

  public SubCommand getSubCommand(String name) {
    return subCommands.containsKey(name) ? subCommands.get(name) : subCommands.get("help");
  }

  public List<String> getAllowedSubCommands(CommandSender sender) {
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
