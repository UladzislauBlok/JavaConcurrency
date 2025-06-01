package org.bloku.chapter6.task.execution.executor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Alerter {

    private static final Logger logger = LoggerFactory.getLogger(Alerter.class);
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Alerter() {}

    public static void alert(final String message) {
        executorService.submit(() -> logger.info(message)); // there could be http call, and we don't want to block calling thread
    }
}
