package me.limbo56.playersettings.settings;

import me.limbo56.playersettings.PlayerSettings;
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
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleSettingWatcher extends MapStore<Setting, Integer> implements SettingWatcher {
    private Player owner;
    private MapStore<Setting, SettingCallback> callbackMap = new MapStore<>();

    public SimpleSettingWatcher(Player owner, Map<String, Setting> settingRegistry, Map<Setting, SettingCallback> callbackMap) {
        // Register stores
        super.register();
        this.callbackMap.register();

        // Initialize fields
        this.owner = owner;
        this.getStored().putAll(convertToIntegerMap(settingRegistry));
        this.callbackMap.getStored().putAll(callbackMap);
    }

    @Override
    public int getValue(Setting setting) {
        return getStored().get(setting);
    }

    @Override
    public void setValue(Setting setting, int value, boolean silent) {
        if (value != setting.getDefaultValue()) {
            if (!getOwner().hasPermission("settings." + setting.getRawName())) {
                PlayerUtils.sendConfigMessage(getOwner(), "settings.noPermission");
                return;
            }
            int maxAllowed = PlayerUtils.getPermissionInt(getOwner(), "settings." + setting.getRawName());
            if (value > 1 && value > maxAllowed) {
                Function<String, String> modifier = s -> s.replaceAll("%max%", String.valueOf(maxAllowed));
                PlayerUtils.sendConfigMessage(getOwner(), "settings.tooLowPermission", modifier);
                return;
            }
            if (value < -maxAllowed) value = -maxAllowed;

            int maxValue = PlayerSettings.getPlugin().getSettingsRegistry().getSetting(setting.getRawName()).getMaxValue();
            if (value > maxValue) {
                Function<String, String> modifier = s -> s.replaceAll("%max%", String.valueOf(maxValue));
                PlayerUtils.sendConfigMessage(getOwner(), "commands.acceptedValues", modifier);
                return;
            }
        }

        getStored().replace(setting, value);
        int finalValue = value;
        Runnable runnable = () -> {
            Bukkit.getPluginManager().callEvent(new SettingUpdateEvent(getOwner(), setting, finalValue));
            if (callbackMap.getStored().containsKey(setting) && !silent)
                callbackMap.getStored().get(setting).notifyChange(setting, getOwner(), finalValue);
        };
        if (Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTask(PlayerSettings.getPlugin(), runnable);
    }

    @Override
    public Map<Setting, SettingCallback> getCallbackMap() {
        return callbackMap.getStored();
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    private Map<Setting, Integer> convertToIntegerMap(Map<String, Setting> settingMap) {
        return settingMap.values().stream()
                .map(setting -> new AbstractMap.SimpleEntry<>(
                        setting,
                        setting.getDefaultValue()
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
