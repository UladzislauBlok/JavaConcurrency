package org.bloku.chapter4.composing.objects.adding_functionality;

import org.bloku.support.annotation.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * To make this approach work, we have to use the same lock that the List uses by using client-side locking or external locking. Client-side locking entails guarding client code that uses some object X with the lock X uses to guard its own state.
 * <p>In order to use client-side locking, you must know what lock X uses.
 */
@ThreadSafe
class SafeListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public boolean putIfAbsent(E x) {
        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent)
                list.add(x);
            return absent;
        }
    }
}
