package org.bloku.chapter5.building.blocks.build.efficient.cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * <p>Caching a Future instead of a value creates the possibility of cache pollution: if a computation is cancelled or fails, future attempts to compute the result will also indicate cancellation or failure.
 * <p>To avoid this, Memoizer removes the Future from the cache if it detects that the computation was cancelled.
 * <p>It might also be desirable to remove the Future upon detecting a RuntimeException if the computation might succeed on a future attempt.
 */
class Memoizer4<IN, OUT> implements Computable<IN, OUT> {
    private final Map<IN, Future<OUT>> cache = new ConcurrentHashMap<>();
    private final Computable<IN, OUT> c;

    public Memoizer4(Computable<IN, OUT> c) {
        this.c = c;
    }

    public OUT compute(final IN arg) throws InterruptedException {
        while (true) {
            Future<OUT> f = cache.get(arg);
            if (f == null) {
                Callable<OUT> eval = () -> c.compute(arg);
                FutureTask<OUT> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }
}
