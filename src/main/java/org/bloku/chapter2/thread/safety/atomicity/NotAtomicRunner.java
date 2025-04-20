package org.bloku.chapter2.thread.safety.atomicity;

import org.bloku.support.domain.Request;
import org.bloku.support.thread.ThreadUtil;

import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;

class NotAtomicRunner {
    private static final String RESULT_MESSAGE_PATTERN = "Expected count: %d, actual count: %d";
    private static final String TASK_COMPLETED_MESSAGE = "Thread %s has completed %d requests";
    private static final int THREADS = 4;
    private static final int REQUESTS_PER_THREAD = 100;

    private static final NotAtomicServerProcessor processor = new NotAtomicServerProcessor();

    /**
     * We expect result to be equal THREADS * REQUESTS_PER_THREAD, but because NotAtomicServerProcessor isn't thread-safe result will be unpredictable
     */
    public static void main(String[] args) {
        Thread[] threads = ThreadUtil.createNThreadsWithTasks(THREADS, new RequestTask());
        ThreadUtil.startThreads(threads);
        ThreadUtil.waitForThreads(threads);
        System.out.printf(RESULT_MESSAGE_PATTERN, THREADS * REQUESTS_PER_THREAD, processor.getCount());
    }

    private static class RequestTask implements Runnable {

        @Override
        public void run() {
            Request request = new Request("dummy");
            range(0, REQUESTS_PER_THREAD)
                    .forEach(i -> processor.process(request));
            System.out.println(TASK_COMPLETED_MESSAGE.formatted(currentThread().getName(), REQUESTS_PER_THREAD));
        }
    }
}
