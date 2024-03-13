package me.limbo56.playersettings.api.event;

import me.limbo56.playersettings.api.Setting;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event fired whenever a {@link Setting}'s value is changed by a {@link Player}.
 *
 * <p>This event provides information related to the setting, player, and the old and new values of
 * the setting. Below is an example demonstrating how to handle this event:
 *
 * <pre>{@code
 * @EventHandler
 * public void onSettingUpdate(SettingUpdateEvent event) {
 *     Player player = event.getPlayer();
 *     Setting setting = event.getSetting();
 *     int newValue = event.getNewValue();
 *     int previousValue = event.getPreviousValue();
 *     player.sendMessage("Setting " + setting.getName() + " changed from " + previousValue + " to " + newValue);
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

  /**
   * Retrieves the new value of the setting that was changed during this event.
   *
   * @return The new value of the setting.
   */
  public int getNewValue() {
    return this.newValue;
  }

  /**
   * Retrieves the previous value of the setting before it was changed.
   *
   * @return The previous value of the setting.
   */
  public int getPreviousValue() {
    return this.previousValue;
  }

  /**
   * Retrieves the player who triggered the setting update event.
   *
   * @return The {@link Player} who triggered the event.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Retrieves the setting that was updated.
   *
   * @return The {@link Setting} that was updated.
   */
  public Setting getSetting() {
    return setting;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
