package me.limbo56.playersettings.configuration;

public class PluginConfiguration extends YmlConfiguration {
    PluginConfiguration() {
        super("config");
    }

    @Override
    protected void addDefaults() {
        addDefault("debug", false);
        addDefault("Mysql.enable", false);
        addDefault("Mysql.host", "localhost");
        addDefault("Mysql.port", 3306);
        addDefault("Mysql.database", "player_settings");
        addDefault("Mysql.user", "root");
        addDefault("Mysql.password", "");
    }
}
