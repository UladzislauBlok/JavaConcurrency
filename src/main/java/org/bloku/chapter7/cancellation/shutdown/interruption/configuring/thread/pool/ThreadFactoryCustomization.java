package org.bloku.chapter7.cancellation.shutdown.interruption.configuring.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadFactoryCustomization {

    private static class MyThreadFactory implements ThreadFactory {
        private final String poolName;

        public MyThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        public Thread newThread(Runnable runnable) {
            return new MyAppThread(runnable, poolName);
        }
    }

    private static class MyAppThread extends Thread {
        public static final String DEFAULT_NAME = "MyAppThread";
        private static volatile boolean debugLifecycle = false;
        private static final AtomicInteger created = new AtomicInteger();
        private static final AtomicInteger alive = new AtomicInteger();
        private static final Logger log = LoggerFactory.getLogger(MyAppThread.class);

        public MyAppThread(Runnable r) {
            this(r, DEFAULT_NAME);
        }

        public MyAppThread(Runnable runnable, String name) {
            super(runnable, name + "-" + created.incrementAndGet());
            setUncaughtExceptionHandler((t, e) -> log.debug("UNCAUGHT in thread {}", t.getName(), e));
        }

        public void run() {
            // Copy debug flag to ensure consistent value throughout.
            boolean debug = debugLifecycle;
            if (debug) log.debug("Created {}", getName());
            try {
                alive.incrementAndGet();
                super.run();
            } finally {
                alive.decrementAndGet();
                if (debug) log.debug("Exiting {}", getName());
            }
        }

        public static int getThreadsCreated() {
            return created.get();
        }

        public static int getThreadsAlive() {
            return alive.get();
        }

        public static boolean getDebug() {
            return debugLifecycle;
        }

        public static void setDebug(boolean b) {
            debugLifecycle = b;
        }
    }
}
