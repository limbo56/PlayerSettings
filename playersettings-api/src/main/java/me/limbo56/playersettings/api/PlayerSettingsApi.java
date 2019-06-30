package me.limbo56.playersettings.api;

/**
 * Interface that represents the API of the plugin
 *
 * @author lim_bo56
 * @since 3/6/2018
 */
public interface PlayerSettingsApi {
    /**
     * Registers the setting provided in the
     * PlayerSettings plugin to be shown
     * in the menu
     *
     * @param setting Setting to be registered
     */
    void registerSetting(Setting setting);

    /**
     * Gets the setting with the name that was provided
     *
     * @param rawName Raw name of the setting
     */
    Setting getSetting(String rawName);

    /**
     * Registers a callback that is called
     * when the setting specified is changed
     *
     * @param setting         Setting to register the callback
     * @param settingCallback The callback
     */
    void registerCallback(Setting setting, SettingCallback settingCallback);
}
