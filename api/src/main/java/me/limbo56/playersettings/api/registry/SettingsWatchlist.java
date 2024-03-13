package me.limbo56.playersettings.api.registry;

import java.util.UUID;
import me.limbo56.playersettings.api.SettingWatcher;
import org.bukkit.plugin.ServicesManager;

/**
 * A registry of {@link SettingWatcher} instances associated with players.
 *
 * <p>The {@code SettingsWatchlist} interface provides methods to retrieve {@code SettingWatcher}
 * instances corresponding to specific players by their unique identifiers. It serves as a central
 * registry for managing setting watchers across the plugin.
 *
 * <p>To obtain an instance of this class, use the Bukkit {@link ServicesManager#load(Class)}
 * method. Below is an example demonstrating how to utilize the {@code SettingsWatchlist}:
 *
 * <pre>{@code
 * Player player;
 * SettingsWatchlist watchlist = Bukkit.getServicesManager().load(SettingsWatchlist.class);
 *
 * // Retrieve the setting watcher for a player
 * SettingWatcher watcher = watchlist.getSettingWatcher(player.getUniqueId());
 *
 * // Change the value of the "example-setting" to 1
 * watcher.setValue("example-setting", 1);
 * }</pre>
 */
public interface SettingsWatchlist {
  /**
   * Retrieves the {@link SettingWatcher} associated with a player identified by their {@link UUID}.
   *
   * @param uuid The unique identifier of the player.
   * @return The {@link SettingWatcher} belonging to the specified player, or {@code null} if not
   *     found.
   */
  SettingWatcher getSettingWatcher(UUID uuid);
}
