package me.limbo56.playersettings.api;

import org.bukkit.entity.Player;

/**
 * Represents a functional interface that is called whenever the value of a {@link Setting} is changed
 *
 * @author David Rodriguez
 */
public interface SettingCallback {

    /**
     * Method called when a setting is changed
     *
     * @param setting  Setting that was changed
     * @param player   Player who it was changed for
     * @param newValue The new value of the setting
     */
    void notifyChange(Setting setting, Player player, boolean newValue);
}
