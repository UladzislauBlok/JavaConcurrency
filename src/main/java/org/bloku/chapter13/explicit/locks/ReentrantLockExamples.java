package org.bloku.chapter13.explicit.locks;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

class ReentrantLockExamples {

    private static class GeneralExample {
        private final Lock lock = new ReentrantLock();

        private void lockSomething() {
            lock.lock();
            try {
                // update object state
                // catch exceptions and restore invariants if necessary
            } finally {
                lock.unlock();
            }
        }
    }

    private static class TryLockExample {

        private final Random rnd = new Random();

        public boolean transferMoney(Account fromAcct, Account toAcct, BigDecimal amount, long timeout, TimeUnit unit)
                throws InterruptedException {
            long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
            long randMod = getRandomDelayModulusNanos(timeout, unit);
            long stopTime = System.nanoTime() + unit.toNanos(timeout);
            while (true) {
                if (fromAcct.lock.tryLock()) {
                    try {
                        if (toAcct.lock.tryLock()) {
                            try {
                                if (fromAcct.balance().compareTo(amount) < 0)
                                    throw new IllegalArgumentException("Insufficient funds");
                                else {
                                    fromAcct.debit(amount);
                                    toAcct.credit(amount);
                                    return true;
                                }
                            } finally {
                                toAcct.lock.unlock();
                            }
                        }
                    } finally {
                        fromAcct.lock.unlock();
                    }
                }
                if (System.nanoTime() > stopTime)
                    return false;
                NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
            }
        }

        // dummy
        private long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
            return 0;
        }

        // dummy
        private long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
            return 0;
        }

        private record Account(ReentrantLock lock, BigDecimal balance) {

            // dummy
            public void debit(BigDecimal amount) {
            }

            // dummy
            public void credit(BigDecimal amount) {
            }
        }
    }

    private static class TimedAndInterruptedTryLockExample {

        private final ReentrantLock lock = new ReentrantLock();

        public boolean trySendOnSharedLine(String message, long timeout, TimeUnit unit) throws InterruptedException {
            long nanosToLock = unit.toNanos(timeout) - estimatedNanosToSend(message);
            if (!lock.tryLock(nanosToLock, NANOSECONDS))
                return false;
            try {
                return sendOnSharedLine(message);
            } finally {
                lock.unlock();
            }
        }

        public boolean sendOnSharedLineInterruptibly(String message) throws InterruptedException {
            lock.lockInterruptibly();
            try {
                return cancellableSendOnSharedLine(message);
            } finally {
                lock.unlock();
            }
        }

        // dummy
        private boolean cancellableSendOnSharedLine(String message) throws InterruptedException {
            return false;
        }

        // dummy
        private boolean sendOnSharedLine(String message) {
            return false;
        }

        // dummy
        private long estimatedNanosToSend(String message) {
            return 0;
        }
    }
}
