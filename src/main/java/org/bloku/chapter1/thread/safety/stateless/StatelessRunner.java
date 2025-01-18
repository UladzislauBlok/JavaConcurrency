package org.bloku.chapter1.thread.safety.stateless;

import org.bloku.support.domain.Request;
import org.bloku.support.domain.Response;
import org.bloku.support.generator.Generator;
import org.bloku.support.generator.StringGenerator;
import org.bloku.support.thread.ThreadUtil;

import java.util.Random;

import static java.lang.Thread.currentThread;

public class StatelessRunner {
    private static final String printPattern = "Request: %s completed with response %s in thread %s%n";
    private static final int THREADS = 4;
    private static final int MIN_WORDS_IN_REQUEST = 3;
    private static final int MAX_WORDS_IN_REQUEST = 8;

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
        private final Generator<String> inputData = new StringGenerator();
        private final Random random = new Random(System.currentTimeMillis());

        @Override
        public void run() {
            int wordsInString = random.nextInt(MIN_WORDS_IN_REQUEST, MAX_WORDS_IN_REQUEST);
            Request request = new Request(inputData.generate(wordsInString));
            Response response = processor.process(request);
            System.out.printf(printPattern, request, response, currentThread().getName());
        }
    }
}
