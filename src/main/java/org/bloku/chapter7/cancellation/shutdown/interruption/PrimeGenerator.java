package org.bloku.chapter7.cancellation.shutdown.interruption;

import org.bloku.support.annotation.GuardedBy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

class PrimeGenerator {

    /**
     * The cancel method sets the cancelled flag, and the main loop polls this flag before searching for the next prime number.<br>
     * For this to work reliably, cancelled must be volatile.
     */
    private static class CancellablePrimeGenerator implements Runnable {

        @GuardedBy("this")
        private final List<BigInteger> primes = new ArrayList<>();
        private volatile boolean cancelled;

        public void run() {
            BigInteger p = BigInteger.ONE;
            while (!cancelled) {
                p = p.nextProbablePrime();
                synchronized (this) {
                    primes.add(p);
                }
            }
        }
        public void cancel() {
            cancelled = true;
        }

        public synchronized List<BigInteger> get() {
            return new ArrayList<>(primes);
        }
    }

    /**
     * If a task that uses above approach, calls a blocking method such as BlockingQueue.put, could have a more serious problem—the task might never check the cancellation flag and therefore might never terminate
     */
    private static class BrokenPrimeProducer extends Thread {

        private final BlockingQueue<BigInteger> queue;
        private volatile boolean cancelled = false;

        BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                BigInteger p = BigInteger.ONE;
                while (!cancelled)
                    queue.put(p = p.nextProbablePrime());
            } catch (InterruptedException consumed) {
                // handle interruption
            }
        }

        public void cancel() {
            cancelled = true;
        }
    }

    /**
     * There are two points in each loop iteration where interruption may be detected: in the blocking put call, and by explicitly polling the interrupted status in the loop header.
     */
    private static class InterruptablePrimeProducer extends Thread {

        private final BlockingQueue<BigInteger> queue;

        InterruptablePrimeProducer(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                BigInteger p = BigInteger.ONE;
                while (!Thread.currentThread().isInterrupted())
                    queue.put(p = p.nextProbablePrime());
            } catch (InterruptedException consumed) {
                /* Allow thread to exit */
            }
        }

        public void cancel() {
            interrupt();
        }
    }
}
