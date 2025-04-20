package org.bloku.chapter5.building.blocks.synchronizers.future.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class FileDownloaderRunner {
    private static final Logger log = LoggerFactory.getLogger(FileDownloaderRunner.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String url = "https://example.com/file.txt";

        FutureTask<String> downloadTask = new FutureTask<>(new FileDownloader(url));

        Thread thread = new Thread(downloadTask);
        thread.start();

        log.info("Waiting for downloading in thread: {}", thread.getName());
        String fileMessage = downloadTask.get();
        log.info("File is downloaded with message: {}", fileMessage);
    }
}
