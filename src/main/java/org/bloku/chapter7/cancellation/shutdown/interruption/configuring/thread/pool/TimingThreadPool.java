package org.bloku.chapter7.cancellation.shutdown.interruption.configuring.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TimingThreadPool in Listing 8.9 shows a custom thread pool that uses beforeExecute, afterExecute, and terminated to add logging and statistics gathering. To measure a task’s runtime, beforeExecute must record the start time and store it somewhere afterExecute can find it.<br>
 * Because execution hooks are called in the thread that executes the task, a value placed in a ThreadLocal by beforeExecute can be retrieved by afterExecute. TimingThreadPool uses a pair of AtomicLongs to keep track of the total number of tasks processed and the total processing time, and uses the terminated hook to print a log message showing the average task time.
 */
class TimingThreadPool extends ThreadPoolExecutor {
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final Logger log = LoggerFactory.getLogger(TimingThreadPool.class);
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        log.info("Thread {}: start {}", t, r);
        startTime.set(System.nanoTime());
    }

    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            log.info("Thread {}: end {}, time={}", t, r, taskTime);
        } finally {
            super.afterExecute(r, t);
        }
    }

    protected void terminated() {
        try {
            log.info("Terminated: avg time={}", totalTime.get() / numTasks.get());
        } finally {
            super.terminated();
        }
    }
}