package me.limbo56.playersettings.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a setting that can be toggled by a {@link Player}
 *
 * @author David Rodriguez
 */
public interface Setting {

    /**
     * Gets the raw name of the setting
     *
     * @return Raw name of the setting
     */
    String getRawName();

    /**
     * Gets the ItemStack defined by in the configuration
     *
     * @return ItemStack
     */
    ItemStack getItem();

    /**
     * Gets the page where the setting is rendered
     *
     * @return The page
     */
    int getPage();

    /**
     * Gets the slot where the setting is rendered
     *
     * @return The slot
     */
    int getSlot();

    /**
     * Gets the default value for the setting
     *
     * @return Default value
     */
    boolean getDefaultValue();
}
