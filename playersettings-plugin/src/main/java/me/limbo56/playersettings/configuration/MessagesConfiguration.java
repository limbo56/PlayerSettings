package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.PlayerSettings;

public class MessagesConfiguration extends YmlConfiguration {
    public MessagesConfiguration(PlayerSettings plugin) {
        super(plugin, "messages");
    }

    @Override
    protected void addDefaults() {
        addDefault("messages.sendMessageOnChange", true);
        addDefault("settings.noPermission", "&cYou don't have permissions to toggle this setting");
        addDefault("settings.selfStackerDisabled", "&cYou have stacker disabled");
        addDefault("settings.targetStackerDisabled", "&cThat player has stacker disabled");
        addDefault("settings.cantStackEntity", "&cYou can't stack that entity");
        addDefault("settings.chatDisabled", "&cYou have chat disabled");
        addDefault("commands.settingNotFound", "&cThe setting specified wasn't found");
        addDefault("commands.acceptedValues", "&cThe only accepted values are: on, off or integers");
        addDefault("commands.setSetting", "&aThe value of %name% &ahas been set to &e%value%");
        addDefault("commands.getSetting", "%name% &7- &e%value%");
    }
}
