package org.bloku.chapter1.thread.safety.locking;

import org.bloku.support.annotation.NotThreadSafe;
import org.bloku.support.domain.NumberRequest;
import org.bloku.support.domain.NumberResponse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@NotThreadSafe
public class UnsafeAtomicProcessor {
    private final AtomicInteger count = new AtomicInteger();
    private final AtomicReference<String> value = new AtomicReference<>();

    public NumberResponse process(final NumberRequest request) {
        count.set(request.number());
        value.set(request.message());
        return new NumberResponse(value.get(), count.get());
    }
}
