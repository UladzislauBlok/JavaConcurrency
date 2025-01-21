package org.bloku.support.generator;

import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;

class IntegerGenerator implements Generator<Integer> {
    private final Random random = new Random(System.currentTimeMillis());

    @Override
    public Integer generate() {
        return random.nextInt();
    }

    @Override
    public Integer generate(final int min, final int max) {
        return random.nextInt(min, max);
    }

    @Override
    public Collection<Integer> generate(final int n) {
        return IntStream.range(0, n)
                .map( i -> generate())
                .boxed()
                .toList();
    }

    @Override
    public Collection<Integer> generate(final int n, final int min, final int max) {
        return IntStream.range(0, n)
                .map( i -> generate(min, max))
                .boxed()
                .toList();
    }
}
