package me.lim_bo56.settings.config;

import me.lim_bo56.settings.managers.ConfigurationManager;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:53:41 AM
 */
public class MessageConfiguration {

    private static ConfigurationManager configurationManager;

    public MessageConfiguration() {
        configurationManager = ConfigurationManager.getMessages();
        loadMessages();
    }

    public static String get(String path) {
        return ((String) configurationManager.get(path)).replace("&", "§").replace("'", "\'");
    }

    private void addDefault(String path, Object value) {
        configurationManager.addDefault(path, value);
    }

    private void loadMessages() {
        addDefault("No-Permissions", "&c&lYOU &7don`t have permissions to do this.");
        addDefault("Player-Stacker-Disabled", "&c&lYOU &7have stacker disabled.");
        addDefault("Target-Stacker-Disabled", "&c&lTHAT &7player has stacker disabled.");
        addDefault("Chat-Disabled", "&c&lYOU &7have the chat disabled.");

        addDefault("Send.Player-Stacker-Disabled", true);
        addDefault("Send.Target-Stacker-Disabled", true);

    }

}
