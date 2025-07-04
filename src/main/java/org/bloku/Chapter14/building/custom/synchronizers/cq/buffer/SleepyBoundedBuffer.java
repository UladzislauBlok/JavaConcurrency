package org.bloku.Chapter14.building.custom.synchronizers.cq.buffer;

import org.bloku.support.annotation.ThreadSafe;

/**
 * SleepyBoundedBuffer attempts to spare callers the inconvenience of implementing the retry logic on each call by encapsulating the same crude “poll and sleep” retry mechanism within the put and take operations.<br>
 * SleepyBoundedBuffer also creates another requirement for the caller—dealing with InterruptedException. When a method blocks waiting for a condition to become true, the polite thing to do is to provide a cancellation mechanism.
 */
@ThreadSafe
class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    private static final long SLEEP_GRANULARITY = 100;

    public SleepyBoundedBuffer(int size) {
        super(size);
    }

    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty())
                    return doTake();
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
}
