package org.bloku.chapter1.thread.safety.locking;

import org.bloku.support.domain.NumberRequest;
import org.bloku.support.domain.NumberResponse;
import org.bloku.support.domain.Tuple;
import org.bloku.support.generator.Generator;
import org.bloku.support.thread.ThreadUtil;

import java.util.Objects;

import static java.lang.Thread.currentThread;
import static org.bloku.support.generator.GeneratorFactory.getIntegerGenerator;
import static org.bloku.support.generator.GeneratorFactory.getStringGenerator;

public class SynchronizedRunner {
    private static final String UNEXPECTED_RESPONSE_MESSAGE = "Got an unexpected response in thead %s.%n Expected: %s, was: %s%n";
    private static final String FINAL_MESSAGE = "End of test";
    private static final int THREADS = 10;
    private static final int REQUEST_PER_THREAD = 20;

    private static final SynchronizedProcessor processor = new SynchronizedProcessor();

    /**
     * After each execution we have expected results
     */
    public static void main(String[] args) {
        Thread[] threadsWithTasks = ThreadUtil.createNThreadsWithTasks(THREADS, new RequestTask());
        ThreadUtil.startThreads(threadsWithTasks);
        ThreadUtil.waitForThreads(threadsWithTasks);
        System.out.println(FINAL_MESSAGE);
    }

    private static class RequestTask implements Runnable {
        private final Generator<String> stringGenerator = getStringGenerator();
        private final Generator<Integer> intGenerator = getIntegerGenerator();

        @Override
        public void run() {
            stringGenerator.generate(REQUEST_PER_THREAD)
                    .stream()
                    .map(this::createRequest)
                    .map(this::process)
                    .filter(this::responseDoesntMatch)
                    .forEach(this::printError);
        }

        private NumberRequest createRequest(String messageString) {
            return new NumberRequest(messageString, intGenerator.generate());
        }

        private Tuple<NumberRequest, NumberResponse> process(NumberRequest request) {
            return new Tuple<>(request, processor.process(request));
        }

        private boolean responseDoesntMatch(Tuple<NumberRequest, NumberResponse> tuple) {
            NumberRequest request = tuple.value1();
            NumberResponse response = tuple.value2();
            return !Objects.equals(request.message(), response.message()) || !Objects.equals(request.number(), response.number());
        }

        private void printError(Tuple<NumberRequest, NumberResponse> tuple) {
            System.out.printf(UNEXPECTED_RESPONSE_MESSAGE, currentThread().getName(), tuple.value1(), tuple.value2());
        }
    }
}
