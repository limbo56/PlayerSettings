package me.limbo56.playersettings.api;

import java.util.UUID;
import org.bukkit.entity.Player;

/**
 * A registry that keeps track of the {@link Setting} values for a specific {@link Player}. This
 * interface allows getting and setting the values of settings for a player.
 */
public interface SettingWatcher {
  /**
   * Retrieves the value of the specified setting.
   *
   * @param settingName The name of the setting.
   * @return The value of the setting.
   */
  int getValue(String settingName);

  /**
   * Sets the value of the specified setting.
   *
   * @param settingName The name of the setting to change.
   * @param value The new value to set.
   * @param silent If set to true, the value will be changed without executing any associated
   *     effects.
   */
  void setValue(String settingName, int value, boolean silent);

  /**
   * Sets the value of the specified setting silently, without triggering any associated effects.
   *
   * @param settingName The name of the setting to change.
   * @param value The new value to set.
   */
  default void setValue(String settingName, int value) {
    setValue(settingName, value, false);
  }

  /**
   * Retrieves the unique identifier of the player associated with this setting watcher.
   *
   * @return The {@link UUID} of the player.
   */
  UUID getOwner();

  /**
   * Retrieves an array containing the names of all settings being watched.
   *
   * @return An array of setting names being watched.
   */
  String[] getWatched();
}
