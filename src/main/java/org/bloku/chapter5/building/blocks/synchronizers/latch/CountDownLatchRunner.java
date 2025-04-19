package org.bloku.chapter5.building.blocks.synchronizers.latch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountDownLatchRunner {
    private static final Logger log = LoggerFactory.getLogger(CountDownLatchRunner.class);
    private static final Context context = new Context();

    public static void main(String[] args) {
        new Thread(new ContextTask()).start();
        context.init();
    }

    private static final class ContextTask implements Runnable {

        @Override
        public void run() {
            log.info("Start task thread task");
            context.callContext();
            log.info("End thread task");
        }
    }
}
