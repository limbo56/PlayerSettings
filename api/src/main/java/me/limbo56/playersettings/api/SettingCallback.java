package me.limbo56.playersettings.api;

import org.bukkit.entity.Player;

/**
 * An object that reacts to changes in the value of a {@link Setting}.
 *
 * <p>A {@code SettingCallback} defines methods to handle changes in the value of a setting. It is
 * typically used to trigger specific actions or effects when a setting is modified.
 */
public interface SettingCallback {
  /**
   * Reacts to a change in the value of a {@link Setting}.
   *
   * @param player The player for whom the setting was changed.
   * @param setting The setting that was changed.
   * @param value The new value of the setting.
   */
  void notifyChange(Player player, Setting setting, int value);

  /**
   * Clears any effects applied to a player by this callback.
   *
   * <p>This method is optional and can be implemented to remove any effects or state changes
   * applied to a player by the callback.
   *
   * @param player The player from whom to clear the effects.
   */
  default void clear(Player player) {}
}
