package org.bloku.Chapter15.atomic.variables.nonblocking.synchronization;

import org.bloku.support.annotation.GuardedBy;
import org.bloku.support.annotation.ThreadSafe;

class SimulatedCASExample {

    /**
     * CAS is an optimistic technique—it proceeds with the update in the hope of success, and can detect failure if another thread has updated the variable since it was last examined.<br>
     * SimulatedCAS illustrates the semantics (but not the implementation or performance) of CAS.
     */
    @ThreadSafe
    private static class SimulatedCAS {
        @GuardedBy("this")
        private int value;

        public synchronized int get() {
            return value;
        }

        public synchronized int compareAndSwap(int expectedValue, int newValue) {
            int oldValue = value;
            if (oldValue == expectedValue)
                value = newValue;
            return oldValue;
        }

        public synchronized boolean compareAndSet(int expectedValue, int newValue) {
            return (expectedValue == compareAndSwap(expectedValue, newValue));
        }
    }

    /**
     * CasCounter in implements a thread-safe counter using CAS.<br>
     * The increment operation follows the canonical form—fetch the old value, transform it to the new value (adding one), and use CAS to set the new value.<br>
     * If the CAS fails, the operation is immediately retried.
     */
    private static class CasCounter {
        private SimulatedCAS value;

        public int getValue() {
            return value.get();
        }

        public int increment() {
            int v;
            do {
                v = value.get();
            }
            while (v != value.compareAndSwap(v, v + 1));
            return v + 1;
        }
    }
}
