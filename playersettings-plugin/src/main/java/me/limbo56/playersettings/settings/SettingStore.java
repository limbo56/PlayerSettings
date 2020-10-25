package me.limbo56.playersettings.settings;

import lombok.RequiredArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.settings.defined.*;
import me.limbo56.playersettings.utils.storage.MapStore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

@RequiredArgsConstructor
public class SettingStore extends MapStore<String, Setting> {

    private final PlayerSettings plugin;
    private final MapStore<Setting, SettingCallback> settingCallbacks = new MapStore<>();

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
        getConfiguration().getKeys(false).forEach(this::parseSetting);
    }

    private void parseSetting(String settingKey) {
        ConfigurationSection settingSection = getConfiguration().getConfigurationSection(settingKey);

        // Check if the section is null
        if (settingSection == null) {
            return;
        }

        // Check if the setting contains commands to be executed
        if (!settingSection.contains("onEnable") || !settingSection.contains("onDisable")) {
            return;
        }

        ConfigurationSetting configurationSetting = new ConfigurationSetting(settingKey);
        List<String> onEnable = settingSection.getStringList("onEnable");
        List<String> onDisable = settingSection.getStringList("onDisable");

        // Register setting on store
        addToStore(configurationSetting);

        // Register setting callback
        addCallback(configurationSetting, (setting, player, value) -> {
            if (value) {
                onEnable.forEach(player::performCommand);
            } else {
                onDisable.forEach(player::performCommand);
            }
        });

        plugin.debug("Loaded setting " + settingSection.getName());
    }

    public void addCallback(Setting setting, SettingCallback settingCallback) {
        settingCallbacks.addToStore(getSetting(setting.getRawName()), settingCallback);
    }

    private Setting getSetting(String rawName) {
        return getStored().get(rawName);
    }

    public MapStore<Setting, SettingCallback> getCallbacks() {
        return settingCallbacks;
    }

    private ItemsConfiguration getConfiguration() {
        return (ItemsConfiguration) plugin.getConfiguration("items");
    }
}
