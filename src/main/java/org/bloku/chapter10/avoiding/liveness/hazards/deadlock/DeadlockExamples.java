package org.bloku.chapter10.avoiding.liveness.hazards.deadlock;

import java.math.BigDecimal;

/**
 * The deadlock in LeftRightDeadlock came about because the two threads attempted to acquire the same locks in a different order. If they asked for the locks in the same order, there would be no cyclic locking dependency and therefore no deadlock. If you can guarantee that every thread that needs locks L and M at the  same time always acquires L and M in the same order, there will be no deadlock
 */
class DeadlockExamples {

    private static class LeftRightDeadlock {
        private final Object left = new Object();
        private final Object right = new Object();

        public void leftRight() {
            synchronized (left) {
                synchronized (right) {
                    doSomething();
                }
            }
        }

        public void rightLeft() {
            synchronized (right) {
                synchronized (left) {
                    doSomethingElse();
                }
            }
        }

        private void doSomething() {
            // dummy
        }

        private void doSomethingElse() {
            // dummy
        }
    }

    /**
     * Deadlock may appear as if all the threads acquire their locks in the same order, but in fact the lock order depends on the order of arguments passed to transferMoney, and these in turn might depend on external inputs
     */
    private static class TransferMoneyDeadlock {

        public void transferMoneyUnsafe(Account fromAccount, Account toAccount, BigDecimal amount) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    if (fromAccount.balance().compareTo(amount) > 0) {
                        fromAccount.debit(amount);
                        toAccount.credit(amount);
                    }
                }
            }
        }

        private static final Object tieLock = new Object();

        /**
         * Version of transferMoney that uses System.identityHashCode to induce a lock ordering. It involves a few extra lines of code, but eliminates the possibility of deadlock<br>
         * In the rare case that two objects have the same hash code, we must use an arbitrary means of ordering the lock acquisitions, and this reintroduces the possibility of deadlock. To prevent inconsistent lock ordering in this case, a third “tiebreaking” lock is used
         */
        public void transferMoney(final Account fromAcct, final Account toAcct, final BigDecimal amount) {
            class Helper {
                public void transfer() {
                    if (fromAcct.balance().compareTo(amount) < 0) {
                        fromAcct.debit(amount);
                        toAcct.credit(amount);
                    }
                }
            }
            int fromHash = System.identityHashCode(fromAcct); // memory hashcode
            int toHash = System.identityHashCode(toAcct); // memory hashcode
            if (fromHash < toHash) {
                synchronized (fromAcct) {
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
            } else if (fromHash > toHash) {
                synchronized (toAcct) {
                    synchronized (fromAcct) {
                        new Helper().transfer();
                    }
                }
            } else {
                synchronized (tieLock) {
                    synchronized (fromAcct) {
                        synchronized (toAcct) {
                            new Helper().transfer();
                        }
                    }
                }
            }
        }

        // dummy
        private record Account(BigDecimal balance) {
            public void debit(BigDecimal amount) {
            }

            public void credit(BigDecimal amount) {
            }
        }
    }
}
