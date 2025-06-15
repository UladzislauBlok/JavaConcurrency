package org.bloku.chapter7.cancellation.shutdown.interruption.stop.thread.service;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

/**
 * Another way to convince a producer-consumer service to shut down is with a poison pill: a recognizable object placed on the queue that means “when you get this, stop.”<br>
 * With a FIFO queue, poison pills ensure that consumers finish the work on their queue before shutting down, since any work submitted prior to submitting the poison pill will be retrieved before the pill;<br>
 * Producers should not submit any work after putting a poison pill on the queue.<br>
 * Poison pills work only when the number of producers and consumers is known.<br>
 * The approach in IndexingService can be extended to multiple producers by having each producer place a pill on the queue and having the consumer stop only when it receives N producers pills.<br>
 * It can be extended to multiple consumers by having each producer place N consumers pills on the queue, though this can get unwieldy with large numbers of producers and consumers.<br>
 * Poison pills work reliably only with unbounded queues.
 */
class IndexingServicePoisonPill {
    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    IndexingServicePoisonPill(BlockingQueue<File> queue, FileFilter fileFilter, File root) {
        this.queue = queue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }

    private class CrawlerThread extends Thread {
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) { /* fall through */ }
            finally {
                while (true) {
                    try {
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e1) { /* retry */ }
                }
            }
        }

        private void crawl(File root) throws InterruptedException {
        }
    }

    public class IndexerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON) {
                        break;
                    } else {
                        indexFile(file);
                    }
                }
            } catch (InterruptedException consumed) {
            }
        }

        private void indexFile(File file) {
            // create index
        }
    }
}
