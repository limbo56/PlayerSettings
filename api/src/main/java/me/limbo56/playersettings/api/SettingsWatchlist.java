package me.limbo56.playersettings.api;

import java.util.UUID;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicesManager;

/**
 * An object that provides access to a registry of {@link SettingWatcher SettingWatchers} belonging
 * to {@link Player Players}.
 *
 * <p>You can get an instance of this class by using the Bukkit {@link ServicesManager#load(Class)}
 * method. Below is an example that shows how to load the SettingsWatchlist, retrieve a setting
 * watcher that belongs to a player, and change the value of an example setting.
 *
 * <pre>{@code
 * Player player;
 * SettingsWatchlist watchlist = Bukkit.getServicesManager().load(SettingsWatchlist.class);
 *
 * // Retrieve player setting watcher
 * SettingWatcher watcher = container.getSettingWatcher(player.getUniqueId());
 *
 * // Change example-setting value to 1
 * watcher.setValue("example-setting", 1, false);
 * }</pre>
 */
public interface SettingsWatchlist {
  /**
   * Gets a {@link SettingWatcher} that belongs to a player by their {@link UUID}
   *
   * @param uuid Unique id of the player
   * @return {@link SettingWatcher} belonging to the player
   */
  SettingWatcher getSettingWatcher(UUID uuid);
}
