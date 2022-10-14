package me.limbo56.playersettings.api.setting;

import org.bukkit.entity.Player;

/** An object that reacts to changes to the value of a {@link Setting} */
public interface SettingCallback {
  /**
   * Function that reacts to {@link Setting} value changes
   *
   * @param setting Setting that was changed
   * @param player Player who it was changed for
   * @param value New value of the setting
   */
  void notifyChange(Setting setting, Player player, int value);
}
