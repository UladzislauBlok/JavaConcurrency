package org.bloku.chapter5.building.blocks.build.efficient.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Memoizer2 certainly has better concurrent behavior than Memoizer1: multiple threads can actually use it concurrently.
 * <p>But it still has some defects as a cache — there is a window of vulnerability in which two threads calling compute at the same time could end up computing the same value.
 */
class Memoizer2<IN, OUT> implements Computable<IN, OUT> {
    private final Map<IN, OUT> cache = new ConcurrentHashMap<>();
    private final Computable<IN, OUT> c;

    public Memoizer2(Computable<IN, OUT> c) {
        this.c = c;
    }

    public OUT compute(IN arg) throws InterruptedException {
        OUT result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
