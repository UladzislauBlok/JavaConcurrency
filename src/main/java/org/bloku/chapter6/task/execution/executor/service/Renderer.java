package org.bloku.chapter6.task.execution.executor.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class Renderer {

    private final ExecutorService executor;

    Renderer(ExecutorService executor) {
        this.executor = executor;
    }

    void renderPage(CharSequence source) {
        List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor); // CompletionService = Executor + BlockingQueue
        for (final ImageInfo imageInfo : info)
            completionService.submit(imageInfo::downloadImage);
        try {
            for (int t = 0, n = info.size(); t < n; t++) {
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private void renderImage(ImageData imageData) { // dummy
    }

    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return List.of(); // dummy
    }

    private static class ImageInfo { // dummy

        private ImageData downloadImage() {
            return new ImageData();
        }
    }
    private static class ImageData { // dummy

    }
}
