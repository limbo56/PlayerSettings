package me.lim_bo56.settings.cmds;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.cmds.args.HelpARG;
import me.lim_bo56.settings.cmds.args.OpenARG;
import me.lim_bo56.settings.cmds.args.ReloadARG;
import me.lim_bo56.settings.utils.ColorUtils;
import me.lim_bo56.settings.utils.Variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:25:01 AM
 */
public class CommandManager implements CommandExecutor {

    private Core plugin;

    public CommandManager(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandHelper helper = new CommandHelper(sender, cmd);

        if (args.length == 0) {
            sender.sendMessage(ColorUtils.Color(" &7-= &6&lPlayer &f&lSettings &7=-"));
            sender.sendMessage(ColorUtils.Color("&ePlugin developed by: &clim_bo56"));
            sender.sendMessage(ColorUtils.Color("&eUse &a/settings help &efor a list of commands."));
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            if (sender instanceof Player) {
                new HelpARG(plugin).executeCommand(sender, cmd, args);
            } else {
                helper.noConsole();
            }
        }

        if (args[0].equalsIgnoreCase("open")) {
            if (sender instanceof Player) {
                new OpenARG(plugin).executeCommand(sender, cmd, args);
            } else {
                helper.noConsole();
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                if (sender.hasPermission(Variables.CMD_RELOAD)) {
                    new ReloadARG(plugin).executeCommand(sender, cmd, args);
                } else {
                    helper.noPermission();
                }
            } else {
                new ReloadARG(plugin).executeCommand(sender, cmd, args);
            }
            return true;
        }

        return false;
    }
}
