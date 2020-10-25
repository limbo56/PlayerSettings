package me.limbo56.playersettings.api;

import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Represents an object that is used to mutate the state of a setting and to store any callbacks
 * that it might need
 *
 * @author David Rodriguez
 */
public interface SettingWatcher {

    /**
     * Gets the value of the provided setting
     *
     * @param setting Setting where value is stored
     * @return The value of the setting
     */
    boolean getValue(Setting setting);

    /**
     * Sets the value of the setting provided
     *
     * @param setting Setting which value will be changed
     * @param value   Value to be set
     * @param silent  If true, it changes the value without activating the side effect
     */
    void setValue(Setting setting, boolean value, boolean silent);

    /**
     * Gets the callback map
     *
     * @return Map with settings callback
     */
    Map<Setting, SettingCallback> getCallbackMap();

    /**
     * Gets the player that this watcher
     * was assigned to
     *
     * @return {@link Player} that owns the setting
     */
    Player getOwner();
}
