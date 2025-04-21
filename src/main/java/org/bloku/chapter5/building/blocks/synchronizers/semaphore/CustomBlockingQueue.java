package org.bloku.chapter5.building.blocks.synchronizers.semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

class CustomBlockingQueue<T> {
    private static final Logger log = LoggerFactory.getLogger(CustomBlockingQueue.class);
    private final Queue<T> queue = new LinkedList<>();
    private final Semaphore semaphore;
    private final int maxSize;

    CustomBlockingQueue(int queueSize) {
        this.semaphore = new Semaphore(0);
        this.maxSize = queueSize;
    }

    synchronized void add(T element) throws InterruptedException {
        log.info("Trying to put element: {}", element);
        while (queue.size() == maxSize)
            wait();
        queue.add(element);
        semaphore.release();
        log.info("Element {} was added", element);
    }

    T poll() {
        log.info("Try to poll element");
        semaphore.acquireUninterruptibly();
        return pollSynchronously();
    }

    synchronized private T pollSynchronously() {
        try {
            log.info("Element {} was polled", queue.peek());
            return queue.poll();
        } finally {
            notifyAll();
        }
    }
}
