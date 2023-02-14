package me.limbo56.playersettings.api.setting;

import org.bukkit.entity.Player;

import java.util.UUID;

/** A registry that keeps track of the {@link Setting} values for a {@link Player}. */
public interface SettingWatcher {
  /**
   * Gets the value of the provided setting
   *
   * @param settingName The name of the setting
   * @return The value of the setting
   */
  int getValue(String settingName);

  /**
   * Sets the value of the setting specified by {@code settingName}
   *
   * @param settingName Name of the setting
   * @param value Value to set
   * @param silent If true, the value will be changed without executing any effects
   */
  void setValue(String settingName, int value, boolean silent);

  /**
   * Gets the player that this watcher belongs to
   *
   * @return {@link UUID} of the {@link Player} that owns this watcher
   */
  UUID getOwner();

  /**
   * Gets an array with the names of all the settings that have a value assigned
   *
   * @return Array of settings being watched
   */
  String[] getWatched();
}
