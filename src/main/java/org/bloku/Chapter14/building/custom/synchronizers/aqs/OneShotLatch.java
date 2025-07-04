package org.bloku.Chapter14.building.custom.synchronizers.aqs;

import org.bloku.support.annotation.ThreadSafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * t has
 * two public methods, await and signal, that correspond to acquisition and release.<br>
 * Initially, the latch is closed; any thread calling await blocks until the latch is opened.<br>
 * Once the latch is opened by a call to signal, waiting threads are released and threads that subsequently arrive at the latch will be allowed to proceed.
 */
@ThreadSafe
class OneShotLatch {
    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {
        protected int tryAcquireShared(int ignored) {
        // Succeed if latch is open (state == 1), else fail
            return (getState() == 1) ? 1 : -1;
        }

        protected boolean tryReleaseShared(int ignored) {
            setState(1); // Latch is now open
            return true; // Other threads may now be able to acquire
        }
    }
}
