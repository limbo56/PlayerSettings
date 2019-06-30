package me.limbo56.playersettings.utils.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface that represents an storage class using a map
 */
public class MapStore<K, V> implements Store<Map<K, V>> {
    private Map<K, V> store;

    public void addToStore(K key, V value) {
        store.put(key, value);
    }

    public void removeFromStore(K key) {
        store.remove(key);
    }

    @Override
    public void register() {
        store = new HashMap<>();
    }

    @Override
    public void unregister() {
        store.clear();
    }

    @Override
    public Map<K, V> getStored() {
        return store;
    }
}