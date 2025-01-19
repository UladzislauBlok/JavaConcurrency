package org.bloku.chapter1.thread.safety.atomicity;

import org.bloku.support.annotation.ThreadSafe;
import org.bloku.support.domain.Request;
import org.bloku.support.domain.Response;

import java.util.concurrent.atomic.AtomicLong;

/**
 *  <p>>Operations A and B are atomic with respect to each other if, from the perspective of a thread executing A, when another thread executes B, either all of B has executed or none of it has. An atomic operation is one that is atomic with respect to all operations, including itself, that operate on the same state.</p>
 *  <p> By replacing the long counter with an AtomicLong, we ensure that all actions that access the counter state are atomic Because the state of the servlet is the state of the counter and the counter is thread-safe, our servlet is once again thread-safe</p>
 */
@ThreadSafe
class AtomicServerProcessor {
    private final AtomicLong count = new AtomicLong(0);

    public Response process(final Request request) {
        count.incrementAndGet();
        return new Response(request.message());
    }

    public long getCount() {
        return count.get();
    }
}
