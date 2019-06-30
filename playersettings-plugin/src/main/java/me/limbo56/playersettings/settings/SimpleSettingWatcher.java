package me.limbo56.playersettings.settings;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.api.SettingUpdateEvent;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.utils.PlayerUtils;
import me.limbo56.playersettings.utils.storage.MapStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleSettingWatcher extends MapStore<Setting, Boolean> implements SettingWatcher {
    private Player owner;
    private MapStore<Setting, SettingCallback> callbackMap = new MapStore<>();

    public SimpleSettingWatcher(Player owner, Map<String, Setting> settingRegistry, Map<Setting, SettingCallback> callbackMap) {
        // Register stores
        super.register();
        this.callbackMap.register();

        // Initialize fields
        this.owner = owner;
        this.getStored().putAll(convertToBooleanMap(settingRegistry));
        this.callbackMap.getStored().putAll(callbackMap);
    }

    @Override
    public boolean getValue(Setting setting) {
        return getStored().get(setting);
    }

    @Override
    public void setValue(Setting setting, boolean value, boolean silent) {
        if (!getOwner().hasPermission("settings." + setting.getRawName())) {
            PlayerUtils.sendConfigMessage(getOwner(), "settings.noPermission");
            return;
        }

        getStored().replace(setting, value);
        Bukkit.getPluginManager().callEvent(new SettingUpdateEvent(getOwner(), setting, value));
        if (callbackMap.getStored().containsKey(setting) && !silent)
            callbackMap.getStored().get(setting).notifyChange(setting, getOwner(), value);
    }

    @Override
    public Map<Setting, SettingCallback> getCallbackMap() {
        return callbackMap.getStored();
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    private Map<Setting, Boolean> convertToBooleanMap(Map<String, Setting> settingMap) {
        return settingMap.values().stream()
                .map(setting -> new AbstractMap.SimpleEntry<>(
                        setting,
                        setting.getDefaultValue()
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
