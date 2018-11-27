package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.utils.storage.MapStore;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationStore implements MapStore<String, YmlConfiguration> {
    private Map<String, YmlConfiguration> configurationMap;

    @Override
    public void register() {
        configurationMap = new HashMap<>();
        addToStore(new ItemsConfiguration());
        addToStore(new MenuConfiguration());
        addToStore(new PluginConfiguration());
    }

    @Override
    public void unregister() {
        configurationMap.clear();
    }

    private void addToStore(YmlConfiguration configuration) {
        addToStore(configuration.getName(), configuration);
    }

    @Override
    public void addToStore(String name, YmlConfiguration configuration) {
        configurationMap.put(name, configuration);
    }

    @Override
    public void removeFromStore(String name) {
        configurationMap.remove(name);
    }

    @Override
    public Map<String, YmlConfiguration> getStored() {
        return configurationMap;
    }
}
