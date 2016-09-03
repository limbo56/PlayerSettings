package me.lim_bo56.settings.managers;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:53:41 AM
 */
public class MessageManager {

    private static ConfigurationManager configurationManager;

    public MessageManager() {
        configurationManager = ConfigurationManager.getMessages();
        loadMessages();
    }

    public static String getMessage(String path) {
        return ((String) configurationManager.get(path)).replace("&", "").replace("'", "\'");
    }

    private void addMessage(String path, String message) {
        configurationManager.addDefault(path, message);
    }

    private void loadMessages() {
        addMessage("No-Permissions", "&c&lYOU &7don`t have permissions to do this.");
        addMessage("Player-Stacker-Disabled", "&c&lYOU &7have stacker disabled.");
        addMessage("Target-Stacker-Disabled", "&c&lTHAT &7player has stacker disabled.");
        addMessage("Chat-Disabled", "&c&lYOU &7have the chat disabled.");

    }

}
