package org.bloku.chapter12.testing.concurrent.programs.testing.correctness;

import java.time.Duration;

class BoundedBufferTest {

    private static final Duration LOCKUP_DETECT_TIMEOUT = Duration.ofSeconds(10);

    void testIsEmptyWhenConstructed() {
        BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++)
            bb.put(i);
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    /**
     * This is an approach to testing blocking operations. It creates a “taker” thread that attempts to take an element from an empty buffer.<br>
     * If take succeeds, it registers failure. The test runner thread starts the taker thread, waits a long time, and then interrupts it.<br>
     * If the taker thread has correctly blocked in the take operation, it will throw InterruptedException, and the catch block for this exception treats this as success and lets the thread exit.<br>
     * The main test runner thread then attempts to join with the taker thread and verifies that the join returned successfully by calling Thread.isAlive; if the taker thread responded to the interrupt, the join should complete quickly.
     */
    void testTakeBlocksWhenEmpty() {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        Thread taker = new Thread(() -> {
            try {
                int unused = bb.take();
                fail(); // if we get here, it’s an error
            } catch (InterruptedException success) {
            }
        });
        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }

    // dummy
    private void fail() {
    }

    // dummy
    private void assertTrue(boolean empty) {
    }

    // dummy
    private void assertFalse(boolean full) {
    }
}
