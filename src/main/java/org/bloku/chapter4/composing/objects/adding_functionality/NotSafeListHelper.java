package org.bloku.chapter4.composing.objects.adding_functionality;

import org.bloku.support.annotation.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Whatever lock the List uses to guard its state, it sure isn’t the lock on the NotSafeListHelper.
 * <p> NotSafeListHelper provides only the illusion of synchronization; the various list operations, while all synchronized, use different locks, which means that putIfAbsent is not atomic relative to other operations on the List. So there is no guarantee that another thread won’t modify the list while putIfAbsent is executing.
 */
@NotThreadSafe
public class NotSafeListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !list.contains(x);
        if (absent)
            list.add(x);
        return absent;
    }
}
