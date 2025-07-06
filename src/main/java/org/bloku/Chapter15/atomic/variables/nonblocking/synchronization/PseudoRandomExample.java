package org.bloku.Chapter15.atomic.variables.nonblocking.synchronization;

import org.bloku.support.annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The performance reversal between locks and atomics at differing levels of contention illustrates the strengths and weaknesses of each. With low to moderate contention, atomics offer better scalability; with high contention, locks offer better contention avoidance.
 */
class PseudoRandomExample {

    @ThreadSafe
    private static class ReentrantLockPseudoRandom implements PseudoRandom {
        private final Lock lock = new ReentrantLock(false);
        private int seed;

        ReentrantLockPseudoRandom(int seed) {
            this.seed = seed;
        }

        public int nextInt(int n) {
            lock.lock();
            try {
                int s = seed;
                seed = xorShift(s);
                int remainder = s % n;
                return remainder > 0 ? remainder : remainder + n;
            } finally {
                lock.unlock();
            }
        }
    }

    @ThreadSafe
    private static class AtomicPseudoRandom implements PseudoRandom {
        private final AtomicInteger seed;

        AtomicPseudoRandom(int seed) {
            this.seed = new AtomicInteger(seed);
        }

        public int nextInt(int n) {
            while (true) {
                int s = seed.get();
                int nextSeed = xorShift(s);
                if (seed.compareAndSet(s, nextSeed)) {
                    int remainder = s % n;
                    return remainder > 0 ? remainder : remainder + n;
                }
            }
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    interface PseudoRandom {

        int nextInt(int upperBound);
    }
}
