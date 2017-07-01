package me.limbo56.settings.cmds.args;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.cmds.BaseCommand;
import me.limbo56.settings.utils.PlayerUtils;
import me.limbo56.settings.utils.Utilities;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by lim_bo56
 * On 8/31/2016
 * At 9:49 PM
 */
public class HelpARG extends BaseCommand {

    public HelpARG(PlayerSettings plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender sender, Command cmd, String[] args) {

        int pageLength = 2;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                Utilities.paginate(sender, plugin.commandHelp, 1, pageLength);
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                if (StringUtils.isNumericSpace((args[1]))) {
                    int page = Integer.parseInt(args[1]);

                    if (page >= 1 && page < pageLength) {
                        Utilities.paginate(sender, plugin.commandHelp, page, pageLength);
                    } else {
                        PlayerUtils.sendMessage(sender, ChatColor.RED + "Failed to find page: " + page + "! Page must be in between 0 and " + 1 + "!");
                    }
                } else {
                    PlayerUtils.sendMessage(sender, ChatColor.RED + "Could not recognize the number: " + args[1] + "!");
                }
            }
        }

    }
}
