package org.bloku.Chapter14.building.custom.synchronizers.cq.buffer;

import org.bloku.support.annotation.GuardedBy;
import org.bloku.support.annotation.ThreadSafe;

/**
 * Classic array-based circular buffer where the buffer state variables (buf, head, tail, and count) are guarded by the buffer’s intrinsic lock.<br>
 * It provides synchronized doPut and doTake methods that are used by subclasses to implement the put and take operations; the underlying state is hidden from the subclasses.
 */
@ThreadSafe
abstract class BaseBoundedBuffer<V> {
    @GuardedBy("this")
    private final V[] buf;
    @GuardedBy("this")
    private int tail;
    @GuardedBy("this")
    private int head;
    @GuardedBy("this")
    private int count;

    protected BaseBoundedBuffer(int capacity) {
        this.buf = (V[]) new Object[capacity];
    }

    protected synchronized final void doPut(V v) {
        buf[tail] = v;
        if (++tail == buf.length)
            tail = 0;
        ++count;
    }

    protected synchronized final V doTake() {
        V v = buf[head];
        buf[head] = null;
        if (++head == buf.length)
            head = 0;
        --count;
        return v;
    }

    public synchronized final boolean isFull() {
        return count == buf.length;
    }

    public synchronized final boolean isEmpty() {
        return count == 0;
    }
}
