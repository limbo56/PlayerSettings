package me.lim_bo56.settings.cmds.args;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.cmds.BaseCommand;
import me.lim_bo56.settings.menus.SettingsMenu;
import me.lim_bo56.settings.utils.Variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:25:08 AM
 */
public class OpenARG extends BaseCommand {

    public OpenARG(Core plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender sender, Command cmd, String[] args) {

        Player player = (Player) sender;

        if (Variables.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            SettingsMenu.openSettings(player);
        }
    }


}
