package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.CommandBase;
import me.limbo56.playersettings.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends CommandBase {

    public ReloadCommand() {
        super(1, "reload", "", "Reloads the configuration of the plugin", "settings.commands.reloadConfig");
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String message = "&e&lWarning\n" +
                "&cIt is not recommended to run this command on a live server.\n" +
                "The command could potentially break the settings plugin " +
                "or lag your whole server.\n" +
                "Please refrain from using it on a live server and only " +
                "while configuring the plugin.";
        PlayerUtils.sendMessage(player, message);

        // Reload config and settings
        PlayerSettings plugin = PlayerSettings.getPlugin();
        plugin.setReloading(true);

        // Save players
        Bukkit.getOnlinePlayers().forEach((players) ->
                plugin.getSPlayer(players.getUniqueId()).unloadPlayer(false)
        );

        // Unregister & register stores
        plugin.getListenerStore().unregister();
        plugin.getSPlayerStore().unregister();
        plugin.getSettingStore().unregister();
        plugin.getConfigurationStore().unregister();
        plugin.getSPlayerStore().register();
        plugin.getConfigurationStore().register();
        plugin.getSettingStore().register();
        plugin.getListenerStore().register();

        // Load online players again
        Bukkit.getOnlinePlayers().forEach(PlayerUtils::loadPlayer);

        plugin.setReloading(false);
        plugin.getLogger().info("The settings configuration has been reloaded");
        PlayerUtils.sendMessage(player, "&aThe settings configuration has been reloaded");
    }
}
