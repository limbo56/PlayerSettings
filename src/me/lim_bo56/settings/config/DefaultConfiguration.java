package me.lim_bo56.settings.config;

import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.utils.Utilities;

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
        if (Utilities.hasRadioPlugin()) {
        	addDefault("Default.Radio", true);

        	addDefault("Radio.type", 1);
        }
        addDefault("Stacker.launch-force", 1);
        addDefault("Speed.level", 1);
        addDefault("Jump.level", 1);
    }

}
