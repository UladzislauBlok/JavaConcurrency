package org.bloku.chapter1.thread.safety.locking;

import org.bloku.support.annotation.ThreadSafe;
import org.bloku.support.domain.NumberRequest;
import org.bloku.support.domain.NumberResponse;

/**
 *  Intrinsic locks in Java act as mutexes (or mutual exclusion locks), which means that at most one thread may own the lock. When thread A attempts to acquire a lock held by thread B, A must wait, or block,untilB releases it. If B never releases the lock, A waits forever.
 */
@ThreadSafe
public class SynchronizedProcessor {
    private volatile int number;
    private volatile String message;

    public synchronized NumberResponse process(final NumberRequest request) {
        number = request.number();
        message = request.message();
        return new NumberResponse(message, number);
    }
}
