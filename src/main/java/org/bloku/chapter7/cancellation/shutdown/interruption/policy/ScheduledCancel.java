package org.bloku.chapter7.cancellation.shutdown.interruption.policy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScheduledCancel {

    /**
     * This is an appealingly simple approach, but it violates the rules: you should know a thread’s interruption policy before interrupting it. Since timedRun can be called from an arbitrary thread, it cannot know the calling thread’s interruption policy. If the task completes before the timeout, the cancellation task that interrupts the thread in which timedRun was called could go off after timedRun has returned to its caller. We don’t know what code will be running when that happens, but the result won’t be good
     */
    private static class BadScheduling {
        private final ScheduledExecutorService cancelExec = Executors.newSingleThreadScheduledExecutor();

        private void timedRun(Runnable r, long timeout, TimeUnit unit) {
            final Thread taskThread = Thread.currentThread();
            cancelExec.schedule(taskThread::interrupt, timeout, unit);
            r.run();
        }
    }

    /**
     * The thread created to run the task can have its own execution policy, and even if the task doesn’t respond to the interrupt, the timed run method can still return to its caller. After starting the task thread, timedRun executes a timed join with the newly created thread. After join returns, it checks if an exception was thrown from the task and if so, rethrows it in the thread calling timedRun. The saved Throwable is shared between the two threads, and so is declared volatile to safely publish it from the task thread to the timedRun thread.
     */
    private static class BetterScheduling {
        private final ScheduledExecutorService cancelExec = Executors.newSingleThreadScheduledExecutor();

        private void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
            class RethrowableTask implements Runnable {
                private volatile Throwable t;

                public void run() {
                    try {
                        r.run();
                    } catch (Throwable t) {
                        this.t = t;
                    }
                }

                void rethrow() {
                    if (t != null)
                        throw new RuntimeException(t);
                }
            }
            RethrowableTask task = new RethrowableTask();
            final Thread taskThread = new Thread(task);
            taskThread.start();
            cancelExec.schedule(taskThread::interrupt, timeout, unit);
            taskThread.join(unit.toMillis(timeout));
            task.rethrow();
        }
    }

    /**
     * If Future#get terminates with a TimeoutException, the task is cancelled via its Future. (To simplify coding, this version calls Future#cancel unconditionally in a finally block, taking advantage of the fact that cancelling a completed task has no effect.) If the underlying computation throws an exception prior to cancellation, it is rethrown from timed-Run, which is the most convenient way for the caller to deal with the exception.
     */
    private static class GoodScheduling {
        private final ExecutorService taskExec = Executors.newSingleThreadExecutor();

        public void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
            Future<?> task = taskExec.submit(r);
            try {
                task.get(timeout, unit);
            } catch (TimeoutException e) {
                // task will be cancelled below
            } catch (ExecutionException e) {
                // exception thrown in task; rethrow
                throw new RuntimeException(e.getCause());
            } finally {
                // Harmless if task already completed
                task.cancel(true); // interrupt if running
            }
        }
    }
}

