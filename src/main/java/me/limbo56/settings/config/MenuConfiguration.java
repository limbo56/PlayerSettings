package me.limbo56.settings.config;

import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.utils.Utilities;

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

        addDefault("Menu.Options.Name", "&6&lSettings");
        addDefault("Menu.Options.Size", 54);

        addDefault("Menu.Items.Enabled.Name", "&a&lOn");
        addDefault("Menu.Items.Enabled.Lore", Arrays.asList("", "&7Click to &cdisable"));
        addDefault("Menu.Items.Enabled.Material", "INK_SACK:10");

        addDefault("Menu.Items.Disabled.Name", "&c&lOff");
        addDefault("Menu.Items.Disabled.Lore", Arrays.asList("", "&7Click to &aenable"));
        addDefault("Menu.Items.Disabled.Material", "INK_SACK:8");

        addDefault("Menu.Items.Speed.Name", "&9Speed II");
        addDefault("Menu.Items.Speed.Material", "SUGAR");
        addDefault("Menu.Items.Speed.Slot", 10);
        addDefault("Menu.Items.Speed.Enabled", true);

        addDefault("Menu.Items.Jump.Name", "&aJump II");
        addDefault("Menu.Items.Jump.Material", "IRON_BOOTS");
        addDefault("Menu.Items.Jump.Slot", 12);
        addDefault("Menu.Items.Jump.Enabled", true);

        addDefault("Menu.Items.Fly.Name", "&bFly");
        addDefault("Menu.Items.Fly.Material", "FEATHER");
        addDefault("Menu.Items.Fly.Slot", 14);
        addDefault("Menu.Items.Fly.Enabled", true);

        addDefault("Menu.Items.Vanish.Name", "&eVanish");
        addDefault("Menu.Items.Vanish.Material", "EXP_BOTTLE");
        addDefault("Menu.Items.Vanish.Slot", 16);
        addDefault("Menu.Items.Vanish.Enabled", true);

        addDefault("Menu.Items.DoubleJump.Name", "&3Double Jump");
        addDefault("Menu.Items.DoubleJump.Material", "RABBIT_FOOT");
        addDefault("Menu.Items.DoubleJump.Slot", 27);
        addDefault("Menu.Items.DoubleJump.Enabled", true);

        addDefault("Menu.Items.Stacker.Name", "&cStacker");
        addDefault("Menu.Items.Stacker.Material", "MAGMA_CREAM");
        addDefault("Menu.Items.Stacker.Slot", 29);
        addDefault("Menu.Items.Stacker.Enabled", true);

        addDefault("Menu.Items.Visibility.Name", "&2Visibility");
        addDefault("Menu.Items.Visibility.Material", "EYE_OF_ENDER");
        addDefault("Menu.Items.Visibility.Slot", 31);
        addDefault("Menu.Items.Visibility.Enabled", true);

        addDefault("Menu.Items.Chat.Name", "&7Chat");
        addDefault("Menu.Items.Chat.Material", "PAPER");
        addDefault("Menu.Items.Chat.Slot", 33);
        addDefault("Menu.Items.Chat.Enabled", true);

        if (Utilities.hasRadioPlugin()) {
            addDefault("Menu.Items.Radio.Name", "&dRadio");
            addDefault("Menu.Items.Radio.Material", "JUKEBOX");
            addDefault("Menu.Items.Radio.Slot", 35);
            addDefault("Menu.Items.Radio.Enabled", true);
        }

        addDefault("worlds-allowed", Collections.singletonList("world"));
    }
}
