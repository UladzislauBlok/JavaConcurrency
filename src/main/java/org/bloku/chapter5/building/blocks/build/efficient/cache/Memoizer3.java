package org.bloku.chapter5.building.blocks.build.efficient.cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * <p>The Memoizer3 implementation is almost perfect: it exhibits very good concurrency (mostly derived from the excellent concurrency of ConcurrentHashMap),
 * the result is returned efficiently if it is already known, and if the computation is in progress by another thread, newly arriving threads wait patiently for the result.
 * <p>It has only one defect—there is still a small window of vulnerability in which two threads might compute the same value. (nonatomic check-then-act sequence)
 */
class Memoizer3<IN, OUT> implements Computable<IN, OUT> {
    private final Map<IN, Future<OUT>> cache = new ConcurrentHashMap<>();
    private final Computable<IN, OUT> c;

    public Memoizer3(Computable<IN, OUT> c) {
        this.c = c;
    }

    public OUT compute(final IN arg) throws InterruptedException {
        Future<OUT> f = cache.get(arg);
        if (f == null) {
            Callable<OUT> eval = () -> c.compute(arg);
            FutureTask<OUT> ft = new FutureTask<>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run(); // call to c.compute happens here
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}

