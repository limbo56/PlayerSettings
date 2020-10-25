package me.limbo56.playersettings.configuration;

import lombok.AllArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.storage.MapStore;

@AllArgsConstructor
public class ConfigurationStore extends MapStore<String, YmlConfiguration> {

    private final PlayerSettings plugin;

    @Override
    public void register() {
        super.register();
        addToStore(new PluginConfiguration(plugin));
        addToStore(new MenuConfiguration(plugin));
        addToStore(new ItemsConfiguration(plugin));
        addToStore(new MessagesConfiguration(plugin));
    }

    private void addToStore(YmlConfiguration configuration) {
        addToStore(configuration.getName(), configuration);
    }
}
