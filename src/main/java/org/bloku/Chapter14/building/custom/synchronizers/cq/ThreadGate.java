package org.bloku.Chapter14.building.custom.synchronizers.cq;

import org.bloku.support.annotation.GuardedBy;
import org.bloku.support.annotation.ThreadSafe;

/**
 * It is easy to develop a re-closeable ThreadGate class using condition waits. ThreadGate lets the gate be opened and closed, providing an await method that blocks until the gate is opened.<br>
 * The open method uses notifyAll because the semantics of this class fail the “one-in, one-out” test for single notification.
 */
@ThreadSafe
public class ThreadGate {
    // CONDITION-PREDICATE: opened-since(n) (isOpen || generation>n)
    @GuardedBy("this")
    private boolean isOpen;
    @GuardedBy("this")
    private int generation;

    public synchronized void close() {
        isOpen = false;
    }

    public synchronized void open() {
        ++generation;
        isOpen = true;
        notifyAll();
    }

    // BLOCKS-UNTIL: opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation)
            wait();
    }
}
