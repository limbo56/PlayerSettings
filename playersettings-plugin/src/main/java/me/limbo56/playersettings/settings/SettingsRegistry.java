package me.limbo56.playersettings.settings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.settings.defined.*;
import me.limbo56.playersettings.utils.storage.MapStore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SettingsRegistry extends MapStore<String, Setting> {
    private @NonNull PlayerSettings plugin;
    private MapStore<Setting, SettingCallback> settingCallbacks = new MapStore<>();

    @Override
    public void register() {
        super.register();
        settingCallbacks.register();

        SpeedSetting speedSetting = new SpeedSetting();
        FlySetting flySetting = new FlySetting();
        JumpSetting jumpSetting = new JumpSetting();
        VanishSetting vanishSetting = new VanishSetting();
        VisibilitySetting visibilitySetting = new VisibilitySetting();

        // Register settings
        addToStore(speedSetting);
        addToStore(new ChatSettings());
        addToStore(flySetting);
        addToStore(jumpSetting);
        addToStore(new StackerSetting());
        addToStore(vanishSetting);
        addToStore(visibilitySetting);

        // Register callbacks
        addCallback(speedSetting, new SpeedSetting.SpeedSettingCallback());
        addCallback(flySetting, new FlySetting.FlySettingCallback());
        addCallback(jumpSetting, new JumpSetting.JumpSettingCallback());
        addCallback(vanishSetting, new VanishSetting.VanishSettingCallback());
        addCallback(visibilitySetting, new VisibilitySetting.VisibilitySettingCallback());

        // Load configuration settings
        loadConfigSettings();
    }

    private void addToStore(Setting setting) {
        addToStore(setting.getRawName(), setting);
    }

    @Override
    public void addToStore(String name, Setting setting) {
        ConfigurationSetting configSetting = getConfiguration().getSetting(setting);
        // Disable if not enabled
        if (!configSetting.isEnabled()) return;
        // Set default value
        super.addToStore(name, configSetting);
        plugin.debug("Added setting to config " + setting.getRawName());
    }

    private void loadConfigSettings() {
        getConfiguration().getKeys(false).forEach(key -> {
            ConfigurationSection settingSection = getConfiguration().getConfigurationSection(key);
            // Check if the setting contains commands to be executed
            if (!settingSection.contains("onEnable") || !settingSection.contains("onDisable")) return;

            ConfigurationSetting configurationSetting = new ConfigurationSetting(key);
            Optional<List<String>> onEnable = Optional.of(settingSection.getStringList("onEnable"));
            Optional<List<String>> onDisable = Optional.of(settingSection.getStringList("onDisable"));

            addToStore(configurationSetting);
            addCallback(configurationSetting, (setting, player, newValue) -> {
                if (newValue > 0) {
                    // that's not ok!
                    onEnable.ifPresent(strings -> strings.forEach(player::performCommand));
                } else {
                    onDisable.ifPresent(strings -> strings.forEach(player::performCommand));
                }
            });

            plugin.debug("Loaded setting " + settingSection.getName());
        });
    }

    public void addCallback(Setting setting, SettingCallback settingCallback) {
        settingCallbacks.addToStore(getSetting(setting.getRawName()), settingCallback);
    }

    public Setting getSetting(String rawName) {
        return getStored().get(rawName);
    }

    public MapStore<Setting, SettingCallback> getCallbacks() {
        return settingCallbacks;
    }

    private ItemsConfiguration getConfiguration() {
        return (ItemsConfiguration) plugin.getConfiguration("items");
    }
}
