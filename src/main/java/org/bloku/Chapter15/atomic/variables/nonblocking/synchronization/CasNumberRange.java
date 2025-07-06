package org.bloku.Chapter15.atomic.variables.nonblocking.synchronization;

import org.bloku.support.annotation.Immutable;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CasNumberRange uses an AtomicReference to an IntPair to hold the state; by using compareAndSet it can update the upper or lower bound without the race conditions.
 */
class CasNumberRange {
    @Immutable
    private record IntPair(int lower, int upper) {
    }

    private final AtomicReference<IntPair> values = new AtomicReference<>(new IntPair(0, 0));

    public int getLower() {
        return values.get().lower;
    }

    public int getUpper() {
        return values.get().upper;
    }

    public void setLower(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i > oldv.upper)
                throw new IllegalArgumentException("Can’t set lower to " + i + " > upper");
            IntPair newv = new IntPair(i, oldv.upper);
            if (values.compareAndSet(oldv, newv))
                return;
        }
    }
}
