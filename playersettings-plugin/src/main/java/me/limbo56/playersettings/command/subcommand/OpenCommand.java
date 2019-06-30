package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.CommandBase;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.settings.SPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends CommandBase {
    public OpenCommand() {
        super(1, "open", "", "Opens the main menu", null);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        SPlayer sPlayer = PlayerSettings.getPlugin().getSPlayer(player.getUniqueId());

        if (PlayerSettings.getPlugin().isReloading()) return;

        SettingsMenu.openMenu(sPlayer, 1);
    }
}
