package me.limbo56.playersettings.listeners;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.storage.CollectionStore;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;

public class ListenerStore implements CollectionStore<Listener> {
    private PlayerSettings plugin;
    private Collection<Listener> listenerCollection;

    public ListenerStore(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        listenerCollection = new ArrayList<>();
        addToStore(new PlayerListener(plugin));
        addToStore(new InventoryListener(plugin));

        // Register listeners
        listenerCollection.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
    }

    @Override
    public void unregister() {
        listenerCollection.clear();
    }

    @Override
    public void addToStore(Listener listener) {
        listenerCollection.add(listener);
    }

    @Override
    public Collection<Listener> getStored() {
        return listenerCollection;
    }
}
