package org.bloku.chapter5.building.blocks.build.efficient.cache;

import org.bloku.support.annotation.GuardedBy;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>HashMap is not thread-safe, so to ensure that two threads do not access the HashMap at the same time, Memoizer1 takes the conservative approach of synchronizing the entire compute method.
 * <p>This ensures thread safety but has an obvious scalability problem: only one thread at a time can execute compute at all.
 * <p>If another thread is busy computing a result, other threads calling compute may be blocked for a long time.
 */
class Memoizer1<IN, OUT> implements Computable<IN, OUT> {
    @GuardedBy("this")
    private final Map<IN, OUT> cache = new HashMap<>();
    private final Computable<IN, OUT> c;

    public Memoizer1(Computable<IN, OUT> c) {
        this.c = c;
    }

    public synchronized OUT compute(IN arg) throws InterruptedException {
        OUT result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
