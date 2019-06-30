package me.limbo56.playersettings.listeners;

import lombok.AllArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.storage.CollectionStore;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class ListenerStore extends CollectionStore<Listener> {
    private PlayerSettings plugin;

    @Override
    public void register() {
        super.register();
        addToStore(new PlayerListener(plugin));
        addToStore(new InventoryListener(plugin));
        addToStore(new ChatSettingListener(plugin));
        addToStore(new StackerSettingListener(plugin));

        // Register listeners
        getStored().forEach(listener ->
                Bukkit.getPluginManager().registerEvents(listener, plugin)
        );
    }

    @Override
    public void unregister() {
        getStored().forEach(HandlerList::unregisterAll);
        super.unregister();
    }
}
