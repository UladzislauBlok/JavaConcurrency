package org.bloku.chapter5.building.blocks.synchronizers.future.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;

class FileDownloader implements Callable<String> {
    private static final Logger log = LoggerFactory.getLogger(FileDownloader.class);

    private final String url;

    FileDownloader(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        log.info("Start downloading from {}", url);
        SECONDS.sleep(5);
        log.info("Download completed");
        return "DOWNLOADED_MESSAGE";
    }
}
