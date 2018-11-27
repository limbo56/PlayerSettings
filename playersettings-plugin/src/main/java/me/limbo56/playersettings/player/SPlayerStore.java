package me.limbo56.playersettings.player;

import me.limbo56.playersettings.utils.PluginLogger;
import me.limbo56.playersettings.utils.storage.MapStore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SPlayerStore implements MapStore<UUID, SPlayer> {
    private Map<UUID, SPlayer> playerMap;

    @Override
    public void register() {
        // Initialize player map
        playerMap = new HashMap<>();
    }

    @Override
    public void unregister() {
        // Clear player map
        playerMap.clear();
    }

    @Override
    public void addToStore(UUID uuid, SPlayer sPlayer) {
        playerMap.put(uuid, sPlayer);
        PluginLogger.info("Player with uuid " + uuid + " has been cached");
    }

    @Override
    public void removeFromStore(UUID uuid) {
        playerMap.remove(uuid);
        PluginLogger.info("Player with uuid " + uuid + " has been removed from the cache");
    }

    @Override
    public Map<UUID, SPlayer> getStored() {
        return playerMap;
    }
}
