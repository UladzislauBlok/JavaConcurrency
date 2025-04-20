package org.bloku.chapter3.sharing.objects.safe_publication;

import org.bloku.support.domain.Request;
import org.bloku.support.domain.Response;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p> VolatileCachedFactorizer in uses a OneValueCache to store the cached number and factors. When a thread sets the volatile cache field to reference a new OneValueCache, the new cached data becomes immediately visible to other threads.
 * <p> The cache-related operations cannot interfere with each other because OneValueCache is immutable and the cache field is accessed only once in each of the relevant code paths. This combination of an immutable holder object for multiple state variables related by an invariant, and a volatile reference used to ensure its timely visibility, allows VolatileCachedFactorizer to be thread-safe even though it does no explicit locking.
 */
class VolatileCachedProcessor {

    private static final Predicate<String> unexpectedFormat = Pattern.compile("\\d+:\\d+").asMatchPredicate().negate();

    private volatile OneValueCache cache = new OneValueCache(null, null);

    public Response service(Request req) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }
        return encodeIntoResponse(factors);
    }

    private Response encodeIntoResponse(BigInteger[] factors) {
        return new Response(Arrays.stream(factors)
                .map(BigInteger::toString)
                .collect(Collectors.joining(",")));
    }

    private BigInteger[] factor(BigInteger i) {
        return IntStream.range(0, 10)
                .mapToObj(n -> i)
                .toArray(BigInteger[]::new);
    }

    private BigInteger extractFromRequest(Request req) {
        if (unexpectedFormat.test(req.message()))
            throw new IllegalArgumentException();
        String[] parsedMsg = req.message().split(":");
        String number = parsedMsg[0];
        int radix = Integer.parseInt(parsedMsg[1]);
        return new BigInteger(number, radix);
    }
}
