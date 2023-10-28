package me.limbo56.playersettings.command.subcommand;

import java.util.ArrayList;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.PluginLogger;
import me.limbo56.playersettings.util.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    PluginLogger.log(ChatColor.YELLOW + "Reloading plugin...");
    Text.from(
            "&cThis command could potentially break the plugin or lag your server. "
                + "Please refrain from using it on a live server and only while configuring the plugin.")
        .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
    PLUGIN.setReloading(true);

    // Unload users
    for (SettingUser user : PLUGIN.getUserManager().getUsers()) {
      Player userPlayer = user.getPlayer();
      user.clearSettingEffects();
      if (userPlayer.getInventory().getHolder() instanceof MenuHolder) {
        userPlayer.closeInventory();
      }
    }
    PLUGIN.getSettingsMenuManager().unloadAll();
    PLUGIN.getUserManager().unloadAll();

    // Disconnect data manager
    PLUGIN.getSettingsDatabase().disconnect();

    // Reload configurations and settings
    PLUGIN.getConfigurationManager().reloadConfigurations();
    PLUGIN.getSettingsManager().reloadSettings();

    // Connect data manager
    PLUGIN.connectSettingsDatabase();

    // Load users
    PLUGIN.getUserManager().loadOnlineUsers();

    // Send successfully reloaded message
    PLUGIN.setReloading(false);
    PluginLogger.log(ChatColor.GREEN + "Plugin reloaded successfully!");
    Text.from("&aThe settings configuration has been reloaded")
        .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }
}
