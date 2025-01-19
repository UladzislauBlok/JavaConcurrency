package org.bloku.chapter1.thread.safety.atomicity;

import org.bloku.support.domain.ExpensiveObject;
import org.bloku.support.thread.ThreadUtil;

public class LazyInitRunner {
    private static final String RESULT_MESSAGE = "Lazy object was initialized %d times";
    private static final int THREADS = 10;

    private static final LazyInitRaceProcessor processor = new LazyInitRaceProcessor();

    /**
     * In most executions {@link ExpensiveObject} will be created N times
     */
    public static void main(String[] args) {
        Thread[] threadsWithTasks = ThreadUtil.createNThreadsWithTasks(THREADS, processor::getInstance);
        ThreadUtil.startThreads(threadsWithTasks);
        ThreadUtil.waitForThreads(threadsWithTasks);
        System.out.printf(RESULT_MESSAGE, processor.getCounter());
    }
}
