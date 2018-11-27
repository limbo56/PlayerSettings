package me.limbo56.playersettings.configuration;

import java.util.Collections;

public class MenuConfiguration extends YmlConfiguration {
    MenuConfiguration() {
        super("menu");
    }

    @Override
    protected void addDefaults() {
        addDefault("Name", "&6&lSettings");
        addDefault("Pages", 1);
        addDefault("Size", 54);
        addDefault("Worlds-Allowed", Collections.singletonList("world"));
    }
}
