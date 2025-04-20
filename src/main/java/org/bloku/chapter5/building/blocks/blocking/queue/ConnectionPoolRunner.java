package org.bloku.chapter5.building.blocks.blocking.queue;

import org.bloku.support.domain.Request;
import org.bloku.support.thread.ThreadUtil;

class ConnectionPoolRunner {
    private static final int THREADS = 10;

    private static final DatabaseProcessor databaseProcessor = new DatabaseProcessor();

    public static void main(String[] args) {
        Thread[] threadsWithTasks = ThreadUtil.createNThreadsWithTasks(THREADS, new RequestTask());
        ThreadUtil.startThreads(threadsWithTasks);
        ThreadUtil.waitForThreads(threadsWithTasks);
    }

    private static final class RequestTask implements Runnable {

        @Override
        public void run() {
            databaseProcessor.process(new Request("Request msg"));
        }
    }
}