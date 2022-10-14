package me.limbo56.playersettings.command.subcommand;

import java.util.ArrayList;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.menu.SettingsInventory;
import me.limbo56.playersettings.settings.DefaultSettingsContainer;
import me.limbo56.playersettings.user.SettingUserManager;
import me.limbo56.playersettings.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadSubCommand extends SubCommand {
  private final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

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
    plugin.getLogger().info(ChatColor.YELLOW + "Reloading plugin...");
    Text.from(
            "&cThis command could potentially break the plugin or lag your server. "
                + "Please refrain from using it on a live server and only while configuring the plugin.")
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
    plugin.setReloading(true);

    // Close all settings inventories
    Bukkit.getOnlinePlayers().stream()
        .filter(online -> online.getInventory().getHolder() instanceof SettingsInventory)
        .forEach(HumanEntity::closeInventory);

    // Unload users
    SettingUserManager userManager = plugin.getUserManager();
    userManager.unloadAllUsers();

    // Disconnect data manager
    plugin.getDataManager().disconnect();

    // Clear data
    ConfigurationManager configurationManager = plugin.getConfigurationManager();
    DefaultSettingsContainer settingManager = plugin.getSettingsContainer();
    configurationManager.reloadConfigurations();
    settingManager.reloadLoadedSettings();
    settingManager.loadSettingsFromConfiguration();

    // Connect data manager
    plugin.initializeDataManager();
    plugin.getDataManager().connect();

    // Load users
    userManager.loadOnlineUsers();

    // Send successfully reloaded message
    plugin.setReloading(false);
    plugin.getLogger().info(ChatColor.GREEN + "Plugin reloaded successfully!");
    Text.from("&aThe settings configuration has been reloaded")
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }
}
