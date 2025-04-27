package org.bloku.chapter5.building.blocks.synchronizers.semaphore;

import org.bloku.support.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.currentThread;
import static org.bloku.support.thread.ThreadUtil.sleepNSeconds;

class CustomQueueRunner {
    private static final Logger log = LoggerFactory.getLogger(CustomQueueRunner.class);
    private static final String MESSAGE_TO_BE_ADDED = "MESSAGE";
    private static final CustomBlockingQueue<String> queue = new CustomBlockingQueue<>(5);

    public static void main(String[] args) {
        Thread[] threadsToPut = ThreadUtil.createNThreadsWithTasks(3, new PutTask());
        Thread[] threadsToPoll = ThreadUtil.createNThreadsWithTasks(3, new PollAndProcessTask());
        ThreadUtil.startThreads(threadsToPut);
        ThreadUtil.startThreads(threadsToPoll);
    }

    private static final class PutTask implements Runnable {

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    queue.add(MESSAGE_TO_BE_ADDED);
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    private static final class PollAndProcessTask implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                String messageFromQueue = queue.poll();
                sleepNSeconds(3);
                log.info("Message {} has been processed successfully", messageFromQueue);
            }
        }
    }
}
