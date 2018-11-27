package me.limbo56.playersettings.utils.storage;

import java.util.Map;

/**
 * Interface that represents an storage class using a map
 */
public interface MapStore<K, V> extends Store<Map<K, V>> {
    /**
     * Adds a value to the storage map
     *
     * @param key   Key of the value
     * @param value Value being stored
     */
    void addToStore(K key, V value);

    /**
     * Removes a value stored in the map
     *
     * @param key Key to be removed
     */
    void removeFromStore(K key);

    /**
     * Get what is being stored
     *
     * @return Stored
     */
    Map<K, V> getStored();
}