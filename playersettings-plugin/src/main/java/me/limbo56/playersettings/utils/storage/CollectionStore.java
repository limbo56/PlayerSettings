package me.limbo56.playersettings.utils.storage;

import java.util.Collection;

/**
 * Interface that represents an storage class using a collection
 */
public interface CollectionStore<T> extends Store<Collection<T>> {
    /**
     * Adds a value to the storage collection
     *
     * @param t Value to be added
     */
    void addToStore(T t);

    /**
     * Get what is being stored
     *
     * @return Stored
     */
    Collection<T> getStored();
}
