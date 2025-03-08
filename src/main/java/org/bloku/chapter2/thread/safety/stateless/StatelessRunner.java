package org.bloku.chapter2.thread.safety.stateless;

import org.bloku.support.domain.Request;
import org.bloku.support.domain.Response;
import org.bloku.support.thread.ThreadUtil;

import static java.lang.Thread.currentThread;
import static org.bloku.support.generator.GeneratorFactory.getStringGenerator;

public class StatelessRunner {
    private static final String TASK_COMPLETED_MESSAGE = "Request: %s completed with response %s in thread %s%n";
    private static final int THREADS = 4;
    private static final int WORDS_IN_REQUEST = 5;

    private static final StatelessServerProcessor processor = new StatelessServerProcessor();

    /**
     * After each execution result must be correct because processor is thread-safe
     */
    public static void main(String[] args) {
        Thread[] threadsWithTasks = ThreadUtil.createNThreadsWithTasks(THREADS, new RequestTask());
        ThreadUtil.startThreads(threadsWithTasks);
        ThreadUtil.waitForThreads(threadsWithTasks);
    }

    private static class RequestTask implements Runnable {
        private static final String DELIMITER = ",";

        @Override
        public void run() {
            String message = String.join(DELIMITER, getStringGenerator().generate(WORDS_IN_REQUEST));
            Request request = new Request(message);
            Response response = processor.process(request);
            System.out.printf(TASK_COMPLETED_MESSAGE, request, response, currentThread().getName());
        }
    }
}
