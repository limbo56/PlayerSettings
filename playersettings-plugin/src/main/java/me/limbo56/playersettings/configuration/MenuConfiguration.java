package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.PlayerSettings;

import java.util.Collections;

public class MenuConfiguration extends YmlConfiguration {
    public MenuConfiguration(PlayerSettings plugin) {
        super(plugin, "menu");
    }

    @Override
    protected void addDefaults() {
        addDefault("Name", "&6&lSettings");
        addDefault("Size", 54);
        addDefault("Worlds-Allowed", Collections.singletonList("world"));
    }
}
