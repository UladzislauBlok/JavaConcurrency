package org.bloku.chapter1.thread.safety.locking;

import org.bloku.support.annotation.NotThreadSafe;
import org.bloku.support.domain.NumberRequest;
import org.bloku.support.domain.NumberResponse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * When multiple variables participate in an invariant, they are not independent: the value of one constrains the allowed value(s) of the others. Thus, when updating one, you must update the others in the same atomic operation.
 * <p>To preserve state consistency, update related state variables in a single atomic operation
 */
@NotThreadSafe
public class UnsafeAtomicProcessor {
    private final AtomicInteger number = new AtomicInteger();
    private final AtomicReference<String> message = new AtomicReference<>();

    public NumberResponse process(final NumberRequest request) {
        number.set(request.number());
        message.set(request.message());
        return new NumberResponse(message.get(), number.get());
    }
}
