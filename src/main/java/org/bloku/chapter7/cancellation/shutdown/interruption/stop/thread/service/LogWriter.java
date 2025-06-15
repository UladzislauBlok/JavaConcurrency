package org.bloku.chapter7.cancellation.shutdown.interruption.stop.thread.service;

import org.bloku.support.annotation.GuardedBy;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class LogWriter {

    /**
     * The way to provide reliable shutdown for LogWriter is to fix the race condition, which means making the submission of a new log message atomic.<br>
     * But we don’t want to hold a lock while trying to enqueue the message, since put could block.<br>
     * Instead, we can atomically check for shutdown and conditionally increment a counter to “reserve” the right to submit a message
     */
    private static class ShuttingDownLogger {
        private final BlockingQueue<String> queue;
        private final LoggerThread loggerThread;
        private final PrintWriter writer;
        @GuardedBy("this")
        private boolean isShutdown;
        @GuardedBy("this")
        private int reservations;

        private ShuttingDownLogger(BlockingQueue<String> queue, LoggerThread loggerThread, PrintWriter writer) {
            this.queue = queue;
            this.loggerThread = loggerThread;
            this.writer = writer;
        }

        public void start() {
            loggerThread.start();
        }

        public void stop() {
            synchronized (this) {
                isShutdown = true;
            }
            loggerThread.interrupt();
        }

        public void log(String msg) throws InterruptedException {
            synchronized (this) {
                if (isShutdown)
                    throw new IllegalStateException();
                ++reservations;
            }
            queue.put(msg);
        }

        private class LoggerThread extends Thread {
            public void run() {
                try {
                    while (true) {
                        try {
                            synchronized (ShuttingDownLogger.this) {
                                if (isShutdown && reservations == 0)
                                    break;
                            }
                            String msg = queue.take();
                            synchronized (ShuttingDownLogger.this) {
                                --reservations;
                            }
                            writer.println(msg);
                        } catch (InterruptedException e) { /* retry */ }
                    }
                } finally {
                    writer.close();
                }
            }
        }
    }

    /**
     * This is a multiple-producer, single-consumer design: any activity calling log is acting as a producer, and the background logger thread is the consumer.<br>
     * If the logger thread falls behind, the BlockingQueue eventually blocks the producers until the logger thread catches up.
     */
    private static class NoShuttingDownLogger {
        private final BlockingQueue<String> queue;
        private final LoggerThread logger;
        private volatile boolean shutdownRequested;

        public NoShuttingDownLogger(PrintWriter writer, int capacity) {
            this.queue = new LinkedBlockingQueue<>(capacity);
            this.logger = new LoggerThread(writer);
        }

        public void start() {
            logger.start();
        }

        public void stop() {
            shutdownRequested = true;
            logger.interrupt();
        }

        /**
         * The implementation of log is a check-then-act sequence: producers could observe that the service has not yet been shut down but still queue messages after the shutdown, again with the risk that the producer might get blocked in log and never become unblocked.
         */
        public void log(String msg) throws InterruptedException {
            if (!shutdownRequested) {
                queue.put(msg);
            } else {
                throw new IllegalStateException("logger is shut down");
            }
        }

        private class LoggerThread extends Thread {
            private final PrintWriter writer;

            private LoggerThread(PrintWriter writer) {
                this.writer = writer;
            }

            public void run() {
                try {
                    while (true)
                        writer.println(queue.take());
                } catch (InterruptedException ignored) { // no shutdown support
                } finally {
                    writer.close();
                }
            }
        }
    }
}
