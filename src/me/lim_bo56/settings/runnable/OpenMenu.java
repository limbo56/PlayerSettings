package me.lim_bo56.settings.runnable;

import me.lim_bo56.settings.menus.SettingsMenu;
import org.bukkit.entity.Player;

/**
 * Created by lim_bo56
 * On 9/3/2016
 * At 11:16 PM
 */
public class OpenMenu implements Runnable {

    private Player player;

    public OpenMenu(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        SettingsMenu.openSettings(player);
    }

}
