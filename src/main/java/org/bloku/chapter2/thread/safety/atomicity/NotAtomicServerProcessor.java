package org.bloku.chapter2.thread.safety.atomicity;

import org.bloku.support.annotation.NotThreadSafe;
import org.bloku.support.domain.Request;
import org.bloku.support.domain.Response;

/**
 *  <p>The increment operation, ++count may look like a single action because of its compact syntax, it is not atomic, which means that it does not execute as a single, indivisible operation.</p>
 *  <p>This is an example of a read-modify-write operation, in which the resulting state is derived from the previous state</p>
 *  <p> If the counter is initially 9,with some unlucky timing each thread could read the value, see that it is 9, add one to it, and each set the counter to 10. This is clearly not what is supposed to happen; an increment got lost along the way, and the hit counter is now permanently off by one</p>
 */
@NotThreadSafe
class NotAtomicServerProcessor {
    private long count = 0;

    public Response process(final Request request) {
        count++;
        return new Response(request.message());
    }

    public long getCount() {
        return count;
    }
}
