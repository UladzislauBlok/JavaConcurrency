package org.bloku.chapter5.building.blocks.producer_consumer_pattern;

import java.io.File;
import java.util.concurrent.BlockingQueue;

class Indexer implements Runnable {
    private final BlockingQueue<File> queue;

    public Indexer(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true)
                indexFile(queue.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File file) {
        System.out.printf("Create index for: %s%n", file);
    }
}
