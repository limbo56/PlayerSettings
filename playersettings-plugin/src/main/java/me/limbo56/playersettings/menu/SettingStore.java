package me.limbo56.playersettings.menu;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.utils.item.ConfigurationItem;
import me.limbo56.playersettings.utils.storage.MapStore;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SettingStore implements MapStore<String, Setting> {
    private PlayerSettings plugin;
    private Map<String, Setting> settingMap;

    public SettingStore(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        settingMap = new HashMap<>();
    }

    @Override
    public void unregister() {
        settingMap.clear();
    }

    @Override
    public void addToStore(String name, Setting setting) {
        settingMap.put(name, setting);

        try {
            plugin.getDatabaseManager().addSetting(setting);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeFromStore(String name) {
        settingMap.remove(name);
    }

    @Override
    public Map<String, Setting> getStored() {
        return settingMap;
    }
}
