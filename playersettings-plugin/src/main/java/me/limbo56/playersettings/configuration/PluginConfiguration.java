package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.PlayerSettings;

public class PluginConfiguration extends YmlConfiguration {
    public PluginConfiguration(PlayerSettings plugin) {
        super(plugin, "config");
    }

    @Override
    protected void addDefaults() {
        addDefault("debug", false);
        addDefault("Database.enabled", false);
        addDefault("Database.host", "localhost");
        addDefault("Database.port", 3306);
        addDefault("Database.name", "player_settings");
        addDefault("Database.user", "root");
        addDefault("Database.password", "");
    }
}
