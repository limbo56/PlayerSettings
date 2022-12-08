package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.menu.SettingsMenuHolder;
import me.limbo56.playersettings.util.PluginLogHandler;
import me.limbo56.playersettings.util.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReloadSubCommand extends SubCommand {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public ReloadSubCommand() {
    super(
        "reload",
        "Reloads the configuration of the plugin",
        "",
        1,
        "playersettings.commands.reload");
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    PluginLogHandler.log(ChatColor.YELLOW + "Reloading plugin...");
    Text.from(
            "&cThis command could potentially break the plugin or lag your server. "
                + "Please refrain from using it on a live server and only while configuring the plugin.")
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
    PLUGIN.setReloading(true);

    // Unload users
    PLUGIN
        .getUserManager()
        .getUsers()
        .forEach(
            user -> {
              Player userPlayer = user.getPlayer();
              user.clearSettingEffects();
              if (userPlayer.getInventory().getHolder() instanceof SettingsMenuHolder) {
                userPlayer.closeInventory();
              }
            });
    PLUGIN.getSettingsMenuManager().unloadAll();
    PLUGIN.getUserManager().unloadAll();

    // Disconnect data manager
    PLUGIN.getSettingsDatabase().disconnect();

    // Reload configurations and settings
    PLUGIN.getConfigurationManager().reloadConfigurations();
    PLUGIN.getSettingsManager().reloadSettings();

    // Connect data manager
    PLUGIN.initializeDataManager();
    PLUGIN.getSettingsDatabase().connect();

    // Load users
    PLUGIN.getUserManager().loadOnlineUsers();

    // Send successfully reloaded message
    PLUGIN.setReloading(false);
    PluginLogHandler.log(ChatColor.GREEN + "Plugin reloaded successfully!");
    Text.from("&aThe settings configuration has been reloaded")
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }
}
