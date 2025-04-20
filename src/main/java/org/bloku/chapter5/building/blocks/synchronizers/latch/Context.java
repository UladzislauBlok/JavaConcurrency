package org.bloku.chapter5.building.blocks.synchronizers.latch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

class Context {
    private static final Logger log = LoggerFactory.getLogger(Context.class);
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    void init() {
        log.info("Start context initialization");
        try {
            SECONDS.sleep(3);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            throw new RuntimeException(e);
        }
        countDownLatch.countDown();
        log.info("Context is ready");
    }

    void callContext() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            currentThread().interrupt();
            throw new RuntimeException(e);
        }
        log.info("Inside context call");
    }
}
