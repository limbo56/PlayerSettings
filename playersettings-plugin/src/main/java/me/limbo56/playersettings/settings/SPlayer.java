package me.limbo56.playersettings.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.SettingWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class SPlayer {

    private final UUID uuid;
    private final SettingWatcher settingWatcher;

    public void loadPlayer() {
        // Try player to load settings
        if (isSqlEnabled()) {
            getPlugin().getDatabaseManager().loadPlayer(this);
        }
    }

    public void unloadPlayer(boolean async) {
        // Try to save player
        if (isSqlEnabled()) {
            getPlugin().getDatabaseManager().savePlayer(this, async);
        }
    }

    private boolean isSqlEnabled() {
        return getPlugin().getConfiguration().getBoolean("Database.enabled");
    }

    private PlayerSettings getPlugin() {
        return PlayerSettings.getPlugin();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
