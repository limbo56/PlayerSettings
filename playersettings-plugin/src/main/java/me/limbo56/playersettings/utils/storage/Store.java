package me.limbo56.playersettings.utils.storage;

/**
 * Interface that represents a container for any type of caching method
 *
 * @author David Rodriguez
 */
public interface Store<T> {

    /**
     * Registers the store
     */
    void register();

    /**
     * Unregisters the store
     */
    void unregister();

    /**
     * Get what is being stored
     *
     * @return Stored
     */
    T getStored();
}
