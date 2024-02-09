package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadSubCommand extends SubCommand {
  private final Messenger messenger;

  public ReloadSubCommand(PlayerSettings plugin) {
    super(
        plugin,
        "reload",
        "Reloads the configuration of the plugin",
        "",
        1,
        "playersettings.commands.reload");
    this.messenger = plugin.getMessenger();
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    PluginLogger.logColored(ChatColor.YELLOW + "Reloading plugin...");
    messenger.sendMessage(
        player,
        "&cThis command could potentially break the plugin or lag your server. Please refrain from using it on a live server and only while configuring the plugin.");
    plugin.setReloading(true);

    // Unload users
    for (SettingUser user : plugin.getUserManager().getUsers()) {
      Player userPlayer = user.getPlayer();
      user.clearSettingEffects();
      if (userPlayer.getInventory().getHolder() instanceof SettingsMenu) {
        userPlayer.closeInventory();
      }
    }
    plugin.getSettingsMenuManager().unloadAll();
    plugin.getUserManager().unloadAll();

    // Disconnect data manager
    plugin.getDataManager().disconnect();

    // Reload configurable components
    plugin.getConfigurationManager().reloadConfigurations();
    plugin.getSettingsManager().reloadSettings();
    plugin.getCommandManager().reloadCommands();

    // Connect data manager
    plugin.getDataManager().connect();

    // Load users
    plugin.getUserManager().loadOnlineUsers();

    // Send successfully reloaded message
    plugin.setReloading(false);
    PluginLogger.logColored(ChatColor.GREEN + "Plugin reloaded successfully!");
    messenger.sendMessage(player, "&aThe settings configuration has been reloaded");
  }
}
