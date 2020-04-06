package me.limbo56.playersettings.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called when a a setting is toggled for a player
 *
 * @author lim_bo56
 * @since 3/6/2018
 */
@AllArgsConstructor
@Getter
public class SettingUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private Setting setting;
    @Accessors(fluent = true)
    private int getValue;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    // fixme what if it is toggled instead??
    public int getPreviousValue() {
        int previous = getValue() - 1;
        if (previous < 0)
            return setting.getMaxValue();
        return previous;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
