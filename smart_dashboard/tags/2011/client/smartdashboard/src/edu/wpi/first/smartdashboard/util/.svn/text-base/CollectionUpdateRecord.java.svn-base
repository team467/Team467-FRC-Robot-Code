package edu.wpi.first.smartdashboard.util;

import java.util.Collection;

/**
 * Represents an add and remove transaction to be applied to a Collection.
 * @author pmalmsten
 */
public class CollectionUpdateRecord<E> {
    Collection<E> add;
    Collection<E> remove;

    public CollectionUpdateRecord(Collection<E> add, Collection<E> remove) {
        this.add = add;
        this.remove = remove;
    }

    public void apply(Collection<E> target) {
        for(E element : remove) {
            target.remove(element);
        }

        for(E element : add) {
            target.add(element);
        }   
    }

    public Collection<E> getAdded() {
        return add;
    }

    public Collection<E> getRemoved() {
        return remove;
    }
}
