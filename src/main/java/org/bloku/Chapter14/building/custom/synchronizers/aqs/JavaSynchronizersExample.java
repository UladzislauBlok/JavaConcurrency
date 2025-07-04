package org.bloku.Chapter14.building.custom.synchronizers.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

class JavaSynchronizersExample extends AbstractQueuedSynchronizer {

    private Thread owner;

    // tryAcquire implementation from nonfair ReentrantLock.
    protected boolean tryAcquire(int ignored) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (compareAndSetState(0, 1)) {
                owner = current;
                return true;
            }
        } else if (current == owner) {
            setState(c + 1);
            return true;
        }
        return false;
    }

    // ---------

    // tryAcquireShared and tryReleaseShared from Semaphore.
    protected int tryAcquireShared(int acquires) {
        while (true) {
            int available = getState();
            int remaining = available - acquires;
            if (remaining < 0
                    || compareAndSetState(available, remaining))
                return remaining;
        }
    }

    protected boolean tryReleaseShared(int releases) {
        while (true) {
            int p = getState();
            if (compareAndSetState(p, p + releases))
                return true;
        }
    }
}
