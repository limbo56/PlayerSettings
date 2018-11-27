package me.limbo56.playersettings.api;

import org.bukkit.inventory.ItemStack;

/**
 * Class that represents a Setting
 *
 * @author lim_bo56
 * @since 3/6/2018
 */

// TODO: Implement page and slot
public class Setting {
    private String rawName;
    private ItemStack item;
    private SettingWatcher settingWatcher;

    public Setting(String rawName, ItemStack item) {
        this(rawName, item, "");
    }

    public Setting(String rawName, ItemStack item, String permission) {
        this(rawName, item, permission, true);
    }

    public Setting(String rawName, ItemStack item, String permission, boolean enabled) {
        this(rawName, item, new SettingWatcher(permission, enabled));
    }

    public Setting(String rawName, ItemStack item, SettingWatcher settingWatcher) {
        this.rawName = rawName;
        this.item = item;
        this.settingWatcher = settingWatcher;
    }

    /**
     * Returns the raw name of the setting
     *
     * @return Raw name of the setting
     */
    public String getRawName() {
        return rawName;
    }

    /**
     * Builds an ItemStack with the values in the configuration
     *
     * @return ItemStack of the setting
     */
    public ItemStack getItem() {
        return item;
    }
    
    /**
     * Returns the watcher of the setting
     *
     * @return Get SettingWatcher
     */
    public SettingWatcher getSettingWatcher() {
        return settingWatcher;
    }
}
