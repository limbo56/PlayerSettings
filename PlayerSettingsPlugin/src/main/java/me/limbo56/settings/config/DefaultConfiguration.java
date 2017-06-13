package me.limbo56.settings.config;

import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.utils.Utilities;

/**
 * Created by lim_bo56
 * On 8/14/2016
 * At 11:24 AM
 */
public class DefaultConfiguration {

    private static ConfigurationManager configurationManager;

    public DefaultConfiguration() {
        configurationManager = ConfigurationManager.getDefault();
        loadData();
    }

    private void addDefault(String path, Object value) {
        configurationManager.addDefault(path, value);
    }

    private void loadData() {
        addDefault("Default.Visibility", true);
        addDefault("Default.Stacker", false);
        addDefault("Default.Chat", true);
        addDefault("Default.Vanish", false);
        addDefault("Default.Fly", false);
        addDefault("Default.Speed", false);
        addDefault("Default.Jump", false);
        addDefault("Default.DoubleJump", false);

        if (Utilities.hasRadioPlugin()) {
            addDefault("Default.Radio", true);
            addDefault("Radio.type", 1);
        } else if (!Utilities.hasRadioPlugin() && configurationManager.contains("Default.Radio")) {
            configurationManager.set("Default.Radio", null);
            configurationManager.set("Radio", null);
        }

        addDefault("Stacker.launch-force", 1);
        addDefault("Speed.level", 2);
        addDefault("Jump.level", 2);
        addDefault("DoubleJump.sound", "ENTITY_BAT_TAKEOFF:0");
        addDefault("DoubleJump.velocity.forward", 1.6);
        addDefault("DoubleJump.velocity.up", 1.0);
    }

}
