package org.bloku.chapter1.thread.safety.locking;

import org.bloku.support.annotation.ThreadSafe;
import org.bloku.support.domain.NumberRequest;
import org.bloku.support.domain.NumberResponse;

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
