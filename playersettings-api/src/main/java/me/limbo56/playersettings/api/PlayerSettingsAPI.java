package me.limbo56.playersettings.api;

import org.bukkit.entity.Player;

/**
 * Interface that represents the API of the plugin
 *
 * @author lim_bo56
 * @since 3/6/2018
 */
public interface PlayerSettingsAPI {
    /**
     * Registers a setting
     *
     * @param setting Setting to be registered
     */
    void registerSetting(Setting setting);

    /**
     * Calls the {@link SettingUpdateEvent}
     *
     * @param player Player who updated the setting
     * @param setting Setting that was updated
     */
    void callSettingUpdate(Player player, Setting setting);
}
