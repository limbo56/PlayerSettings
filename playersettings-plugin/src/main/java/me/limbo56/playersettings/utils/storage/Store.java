package me.limbo56.playersettings.utils.storage;

/**
 * Interface that represents an storage class
 */
public interface Store<T> extends Cloneable {
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
