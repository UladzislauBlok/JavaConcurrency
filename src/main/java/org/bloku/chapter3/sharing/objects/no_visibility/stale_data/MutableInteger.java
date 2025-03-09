package org.bloku.chapter3.sharing.objects.no_visibility.stale_data;

import org.bloku.support.annotation.NotThreadSafe;
import org.bloku.support.annotation.ThreadSafe;

public class MutableInteger {

    /**
     * This class is not thread-safe because the value field is accessed from both get and set without synchronization. Among other hazards, it is susceptible to stale values: if one thread calls set, other threads calling get may or may not see that update.
     */
    @NotThreadSafe
    public static class MutableNonSafeInteger {
        private int value;

        public int get() {
            return value;
        }

        public void set(int value) {
            this.value = value;
        }
    }

    /**
     * This class is thread-safe because of synchronization using the same monitor for both methods. Synchronizing only the setter would not be sufficient: threads calling get would still be able to see stale values.
     */
    @ThreadSafe
    public static class MutableSafeInteger {
        private int value;

        public synchronized int get() {
            return value;
        }

        public synchronized void set(int value) {
            this.value = value;
        }
    }
}
