package org.bloku.chapter1.thread.safety.atomicity;

import org.bloku.support.annotation.NotThreadSafe;
import org.bloku.support.domain.ExpensiveObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Check-then-act race condition: you observe something to be true (file X doesn’t exist) and then take action based on that observation (create X); but in fact the observation could have become invalid between the time you observed it and the time you acted on it (someone else created X in the meantime), causing a problem (unexpected exception, overwritten data, file corruption).
 */
@NotThreadSafe
class LazyInitRaceProcessor {
    private final AtomicInteger counter = new AtomicInteger(0);
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject();
            counter.incrementAndGet();
        }
        return instance;
    }

    public int getCounter() {
        return counter.get();
    }
}
