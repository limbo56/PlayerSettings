package me.limbo56.settings.cmds.args;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.cmds.BaseCommand;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.menu.SettingsMenu;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:25:08 AM
 */
public class OpenARG extends BaseCommand {

    public OpenARG(PlayerSettings plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;

        int maxBounds = ConfigurationManager.getMenu().getInt("Menu.Options.Size") - 10;

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {
            try {
                SettingsMenu.openSettings(player);
            } catch (ArrayIndexOutOfBoundsException e) {
                player.sendMessage(ColorUtils.Color(
                        Cache.CHAT_TITLE +
                                "&4An item's slot is out of bounds, the max bounds is " + maxBounds +
                                ", the item slot should not be over " + maxBounds + "!" +
                                " &cPlease check the menu.yml configuration, and set item slots between the boundaries (0 - " + maxBounds + ")."));

                if (ConfigurationManager.getConfig().getBoolean("Debug"))
                    PlayerSettings.getInstance().log("executeCommand: Item slot out of bounds");
            } catch (IllegalArgumentException exception) {
                player.sendMessage(ColorUtils.Color(
                        Cache.CHAT_TITLE + "&cThe menu size must be a multiple of 9."));

                if (ConfigurationManager.getConfig().getBoolean("Debug"))
                    PlayerSettings.getInstance().log("executeCommand: Chest size is not multiple of 9");
            }

        } else {
            if (player.isOp() || player.hasPermission("settings.*")) {
                player.sendMessage(ColorUtils.Color(Cache.CHAT_TITLE
                        + "The plugin is not enabled in this world, "
                        + "please add the name of this world to the list in menu.yml to enable it"));
            }
        }
    }

}
