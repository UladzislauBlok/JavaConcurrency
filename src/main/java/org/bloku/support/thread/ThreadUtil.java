package org.bloku.support.thread;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ThreadUtil {
    private ThreadUtil() {
    }

    public static Thread[] createNThreadsWithTasks(final int n, Runnable task) {
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(task);
        }
        return threads;
    }

    public static void startThreads(Thread[] threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static void waitForThreads(Thread[] threads) {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public static void sleepNSeconds(final int seconds) {
        try {
            SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public static void sleepNMilliseconds(final int milliseconds) {
        try {
            MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
