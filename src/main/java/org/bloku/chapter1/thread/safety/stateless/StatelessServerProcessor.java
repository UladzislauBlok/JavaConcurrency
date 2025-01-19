package org.bloku.chapter1.thread.safety.stateless;

import org.bloku.support.annotation.ThreadSafe;
import org.bloku.support.domain.Request;
import org.bloku.support.domain.Response;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

/**
 * Since the actions of a thread accessing a stateless object cannot affect the correctness of operations in other threads, stateless objects are thread safe.
 */
@ThreadSafe
class StatelessServerProcessor {
    private static final String RESPONSE_PATTERN = "%s:%d";
    private static final String DELIMITER = ",";

    public Response process(final Request request) {
        return new Response(
                Arrays.stream(request.message().split(DELIMITER))
                        .map(this::handle)
                        .collect(joining(DELIMITER)));
    }

    private String handle(final String str) {
        return RESPONSE_PATTERN.formatted(str, str.length());
    }
}
