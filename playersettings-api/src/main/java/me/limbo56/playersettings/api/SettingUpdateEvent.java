package me.limbo56.playersettings.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called when a a setting is toggled for a player
 *
 * @author David Rodriguez
 */
@AllArgsConstructor
@Getter
public class SettingUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Setting setting;
    private final boolean value;

    /**
     * Resolves the current value of the setting that was changed in this event
     *
     * @return Current value of the setting
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     * Resolves the previous value that this setting had before it was changed
     *
     * @return Previous value of the setting
     */
    public boolean getPreviousValue() {
        return !value;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
