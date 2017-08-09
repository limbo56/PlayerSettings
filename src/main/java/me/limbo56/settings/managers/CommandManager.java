package me.limbo56.settings.managers;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.cmds.CommandHelper;
import me.limbo56.settings.cmds.args.HelpARG;
import me.limbo56.settings.cmds.args.OpenARG;
import me.limbo56.settings.cmds.args.ReloadARG;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.ColorUtils;
import me.limbo56.settings.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:25:01 AM
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private PlayerSettings plugin;

    public CommandManager(PlayerSettings plugin) {
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
                if (sender.hasPermission(Cache.CMD_RELOAD)) {
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

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            PlayerUtils.sendMessage(sender, Cache.CHAT_TITLE + ChatColor.RED + "This command can only be run in-game!");
        }

        String[] arguments = new String[]{"help", "open", "reload"};

        if (args[0].equalsIgnoreCase("")) {
            return Arrays.asList(arguments);
        }

        if (!args[0].equalsIgnoreCase("")) {
            for (String string : arguments) {
                if (string.startsWith(args[0])) {
                    return Collections.singletonList(string);
                }
            }
        }

        return null;
    }
}
