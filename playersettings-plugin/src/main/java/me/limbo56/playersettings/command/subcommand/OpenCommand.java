package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.CommandBase;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.limbo56.playersettings.utils.PlayerUtils.isAllowedWorld;

public class OpenCommand extends CommandBase {
    public OpenCommand() {
        super(1, "open", "", "Opens the main menu", null);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        SPlayer sPlayer = PlayerSettings.getPlugin().getSPlayer(player.getUniqueId());

        if (PlayerSettings.getPlugin().isReloading()) return;
        if (!isAllowedWorld(player.getWorld().getName())) {
            if (player.isOp()) {
                String warnMessage = "&cTo enable the menu on this world, " +
                        "add the current world's name to the 'Worlds-Allowed' " +
                        "section in the 'menu.yml' file.";
                PlayerUtils.sendMessage(player, warnMessage);
            }
            return;
        }

        SettingsMenu.openMenu(sPlayer, 1);
    }
}
