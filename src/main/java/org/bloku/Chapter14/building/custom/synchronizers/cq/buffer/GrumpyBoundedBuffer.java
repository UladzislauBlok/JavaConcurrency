package org.bloku.Chapter14.building.custom.synchronizers.cq.buffer;

import org.bloku.support.annotation.ThreadSafe;

/**
 * The simplification in implementing the buffer (forcing the caller to manage the state dependence) is more than made up for by the substantial complication in using it, since now the caller must be prepared to catch exceptions and possibly retry for every buffer operation.
 */
@ThreadSafe
class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    public GrumpyBoundedBuffer(int size) {
        super(size);
    }

    synchronized void put(V v) throws BufferFullException {
        if (isFull())
            throw new BufferFullException();
        doPut(v);
    }

    synchronized V take() throws BufferEmptyException {
        if (isEmpty())
            throw new BufferEmptyException();
        return doTake();
    }

    static class BufferFullException extends IllegalStateException {
    }

    static class BufferEmptyException extends Exception {
    }

    private static class Client {
        private static final long SLEEP_GRANULARITY = 100;
        GrumpyBoundedBuffer<String> buffer = new GrumpyBoundedBuffer<>(1);

        private void call() throws InterruptedException {
            while (true) {
                try {
                    String item = buffer.take();
                    // use item
                    break;
                } catch (BufferEmptyException e) {
                    Thread.sleep(SLEEP_GRANULARITY);
                }
            }
        }
    }
}
