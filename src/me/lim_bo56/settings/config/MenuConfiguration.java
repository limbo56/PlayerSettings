package me.lim_bo56.settings.config;

import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.utils.Utilities;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by lim_bo56
 * On 8/31/2016
 * At 6:42 PM
 */
public class MenuConfiguration {

    private static ConfigurationManager configurationManager;

    public MenuConfiguration() {
        configurationManager = ConfigurationManager.getMenu();
        loadMenu();
    }

    public static String get(String path) {
        return ((String) configurationManager.get(path)).replace("&", "§").replace("'", "\'");
    }

    private void addDefault(String path, Object value) {
        configurationManager.addDefault(path, value);
    }

    private void loadMenu() {

        addDefault("Menu.Name", "&6&lSettings");

        addDefault("Menu.Items.Enabled.Name", "&a&lOn");
        addDefault("Menu.Items.Enabled.Lore", Arrays.asList("", "&7Click to &aenable"));

        addDefault("Menu.Items.Disabled.Name", "&c&lOff");
        addDefault("Menu.Items.Disabled.Lore", Arrays.asList("", "&7Click to &cdisable"));


        addDefault("Menu.Items.Speed.Name", "&9Speed II");
        addDefault("Menu.Items.Speed.Material", "SUGAR");

        addDefault("Menu.Items.Jump.Name", "&aJump II");
        addDefault("Menu.Items.Jump.Material", "IRON_BOOTS");

        addDefault("Menu.Items.Fly.Name", "&bFly");
        addDefault("Menu.Items.Fly.Material", "FEATHER");

        addDefault("Menu.Items.Vanish.Name", "&eVanish");
        addDefault("Menu.Items.Vanish.Material", "EXP_BOTTLE");

        addDefault("Menu.Items.Stacker.Name", "&cStacker");
        addDefault("Menu.Items.Stacker.Material", "MAGMA_CREAM");

        addDefault("Menu.Items.Visibility.Name", "&2Visibility");
        addDefault("Menu.Items.Visibility.Material", "EYE_OF_ENDER");

        addDefault("Menu.Items.Chat.Name", "&7Chat");
        addDefault("Menu.Items.Chat.Material", "PAPER");
        
        if (Utilities.hasRadioPlugin()) {
        	addDefault("Menu.Items.Radio.Name", "&dRadio");
        	addDefault("Menu.Items.Radio.Material", "JUKEBOX");
        }

        addDefault("worlds-allowed", Collections.singletonList("world"));

    }
}
