package me.limbo56.playersettings.player;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.utils.storage.MapStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SPlayer {
    private UUID uuid;
    private MapStore<String, Setting> settingStore;

    public SPlayer(UUID uuid, MapStore<String, Setting> settingStore) {
        this.uuid = uuid;
        this.settingStore = settingStore;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean hasSetting(String setting) {
        return settingStore.getStored().containsKey(setting);
    }

    public Setting getSetting(String setting) {
        return settingStore.getStored().get(setting);
    }

    public MapStore<String, Setting> getSettingStore() {
        return settingStore;
    }
}
