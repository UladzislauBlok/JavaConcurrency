package org.bloku.chapter7.cancellation.shutdown.interruption.implicit.couplings;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RenderPageTask submits two additional tasks to the Executor to fetch the page header and footer, renders the page body, waits for the results of the header and footer tasks, and then combines the header, body, and footer into the finished page.<br>
 * With a single-threaded executor, ThreadDeadlock will always deadlock
 */
class ThreadDeadlock {

    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100));

    // Using a bounded queue and the caller-runs policy, after all the pool threads were occupied and the work queue filled up the next task would be executed in the main thread during the call to execute
    {
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            return header.get() + page + footer.get();
        }

        private String renderBody() {
            // render
            return "RenderedPage";
        }
    }

    private static class LoadFileTask implements Callable<String> {
        private final String fileName;

        private LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public String call() {
            // load file
            return fileName + "page";
        }
    }
}
