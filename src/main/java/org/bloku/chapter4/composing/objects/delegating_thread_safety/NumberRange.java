package org.bloku.chapter4.composing.objects.delegating_thread_safety;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * NumberRange is not thread-safe; it does not preserve the invariant that constrains lower and upper. The setLower and setUpper methods attempt to respect this invariant, but do so poorly. Both setLower and setUpper are check-then-act sequences, but they do not use sufficient locking to make them atomic
 * <p>NumberRange could be made thread-safe by using locking to maintain its invariants, such as guarding lower and upper with a common lock. It must also avoid publishing lower and upper to prevent clients from subverting its invariants.
 */
class NumberRange {
    // INVARIANT: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        // Warning -- unsafe check-then-act
        if (i > upper.get())
            throw new IllegalArgumentException("can’t set lower to " + i + " > upper");
        lower.set(i);
    }

    public void setUpper(int i) {
        // Warning -- unsafe check-then-act
        if (i < lower.get())
            throw new IllegalArgumentException("can’t set upper to " + i + " < lower");
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}
