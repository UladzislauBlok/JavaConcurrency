package org.bloku.chapter4.composing.objects.adding_functionality;

import org.bloku.support.annotation.ThreadSafe;

import java.util.Vector;

/**
 * Extension is more fragile than adding code directly to a class, because the implementation of the synchronization policy is now distributed over multiple, separately maintained source files.
 *<p> If the underlying class were to change its synchronization policy by choosing a different lock to guard its state variables, the subclass would subtly and silently break, because it no longer used the right lock to control concurrent access to the base class’s state.
 *<p> The synchronization policy of Vector is fixed by its specification, so BetterVector would not suffer from this problem.
 */
@ThreadSafe
public class BetterVector<E> extends Vector<E> {

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent)
            add(x);
        return absent;
    }
}

