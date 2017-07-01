package me.limbo56.settings.cmds.args;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.cmds.BaseCommand;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.utils.ColorUtils;
import me.limbo56.settings.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by lim_bo56
 * On 8/14/2016
 * At 11:04 AM
 */
public class ReloadARG extends BaseCommand {

    public ReloadARG(PlayerSettings plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender sender, Command cmd, String[] args) {
        try {
            ConfigurationManager.getMessages().reloadConfig();
            ConfigurationManager.getDefault().reloadConfig();

            if (ConfigurationManager.getDefault().getBoolean("Default.Fly") && ConfigurationManager.getDefault().getBoolean("Default.DoubleJump")) {
                PlayerSettings.getInstance().log("ALERT: You cannot have both Fly and DoubleJump enabled by default! Disabling DoubleJump");
                ConfigurationManager.getDefault().set("Default.DoubleJump", false);
                ConfigurationManager.getDefault().saveConfig();
                ConfigurationManager.getDefault().reloadConfig();
            }

            ConfigurationManager.getMenu().reloadConfig();
            PlayerSettings.getInstance().reloadConfig();

            if (!ConfigurationManager.getConfig().getBoolean("MySQL.enable"))
                PlayerUtils.loadOnlinePlayers();

            sender.sendMessage(ColorUtils.Color("&aAll files have been reloaded correctly!"));
        } catch (Exception exception) {
            sender.sendMessage(ColorUtils.Color("&cAn error occurred while reloading files!"));
        }
    }

}
