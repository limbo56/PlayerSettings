package me.limbo56.playersettings.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called when player enabled/disables a setting
 *
 * @author lim_bo56
 * @since 3/6/2018
 */
public class SettingUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private Player player;
    private Setting setting;

    public SettingUpdateEvent(Player player, Setting setting) {
        this.player = player;
        this.setting = setting;
    }

    public Player getPlayer() {
        return player;
    }

    public Setting getSetting() {
        return setting;
    }

    public boolean getTo() {
        return setting.getSettingWatcher().isEnabled();
    }

    public boolean getFrom() {
        return !setting.getSettingWatcher().isEnabled();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public HandlerList getHandlerList() {
        return HANDLERS;
    }
}
