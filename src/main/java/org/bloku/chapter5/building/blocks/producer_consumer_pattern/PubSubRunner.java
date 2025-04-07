package org.bloku.chapter5.building.blocks.producer_consumer_pattern;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class PubSubRunner {
    private static final int BOUND = 100;

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(BOUND);
        FileFilter filter = file -> true;
        for (File root : roots)
            new Thread(new FileCrawler(queue, filter, root)).start();
        for (int i = 0; i < 5; i++)
            new Thread(new Indexer(queue)).start();
    }
}
