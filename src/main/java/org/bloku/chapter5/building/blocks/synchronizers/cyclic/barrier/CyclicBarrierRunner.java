package org.bloku.chapter5.building.blocks.synchronizers.cyclic.barrier;

import org.bloku.support.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.currentThread;
import static org.bloku.support.thread.ThreadUtil.sleepNSeconds;

public class CyclicBarrierRunner {
    private static final Logger log = LoggerFactory.getLogger(CyclicBarrierRunner.class);
    private static final Random random = new Random();
    private static final CyclicBarrier barrier = new CyclicBarrier(2, () -> log.info("-----------------------"));

    public static void main(String[] args) {
        Thread[] threadsWithTask = ThreadUtil.createNThreadsWithTasks(2, new RandomTimeTask());
        ThreadUtil.startThreads(threadsWithTask);
    }

    private static final class RandomTimeTask implements Runnable {

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) {
                    log.info("Start processing task");
                    sleepNSeconds(random.nextInt(5));
                    log.info("Processing is finished");
                    barrier.await();
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
