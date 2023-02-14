package me.limbo56.playersettings.api.event;

import me.limbo56.playersettings.api.setting.Setting;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An {@link Event} that is fired whenever a {@link Setting}'s value is changed by a {@link Player}
 *
 * <p>The event provides information related to the setting, owner, and value. The code below is an
 * example that uses the event to send the player a message that says the setting changed and its
 * new value:
 *
 * <pre>{@code
 * @EventHandler
 * public void onSettingUpdate(SettingUpdateEvent event) {
 *   Player player = event.getPlayer();
 *   String settingName = event.getSetting().getName();
 *   boolean value = event.getValue();
 *   player.sendMessage(settingName + " - " + value);
 * }
 * }</pre>
 */
public class SettingUpdateEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final Player player;
  private final Setting setting;
  private final int newValue;
  private final int previousValue;

  public SettingUpdateEvent(Player player, Setting setting, int previousValue, int newValue) {
    this.player = player;
    this.setting = setting;
    this.newValue = newValue;
    this.previousValue = previousValue;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  /**
   * Gets the new value of the setting that was changed during this event
   *
   * @return new value of the setting
   */
  public int getNewValue() {
    return this.newValue;
  }

  /**
   * Gets the previous value that this setting had before it was changed
   *
   * @return previous value of the setting
   */
  public int getPreviousValue() {
    return this.previousValue;
  }

  public Player getPlayer() {
    return player;
  }

  public Setting getSetting() {
    return setting;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
