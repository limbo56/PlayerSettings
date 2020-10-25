package me.limbo56.playersettings.utils.storage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A base implementation of a {@link Store} using a {@link Collection}
 *
 * @author David Rodriguez
 */
public class CollectionStore<T> implements Store<Collection<T>> {

    private Collection<T> store;

    public void addToStore(T t) {
        store.add(t);
    }

    @Override
    public void register() {
        store = new ArrayList<>();
    }

    @Override
    public void unregister() {
        store.clear();
    }

    @Override
    public Collection<T> getStored() {
        return store;
    }
}
