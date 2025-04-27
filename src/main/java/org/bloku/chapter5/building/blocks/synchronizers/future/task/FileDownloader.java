package org.bloku.chapter5.building.blocks.synchronizers.future.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

import static org.bloku.support.thread.ThreadUtil.sleepNSeconds;

class FileDownloader implements Callable<String> {
    private static final Logger log = LoggerFactory.getLogger(FileDownloader.class);

    private final String url;

    FileDownloader(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        log.info("Start downloading from {}", url);
        sleepNSeconds(5);
        log.info("Download completed");
        return "DOWNLOADED_MESSAGE";
    }
}
