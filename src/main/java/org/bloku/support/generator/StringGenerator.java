package org.bloku.support.generator;

import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;

class StringGenerator implements Generator<String> {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;
    private static final char[] CHARS = new char[] {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7'
    };

    private final Random random = new Random(System.currentTimeMillis());

    @Override
    public String generate() {
        return generate(MIN_LENGTH, MAX_LENGTH);
    }

    @Override
    public String generate(final int minLength, final int maxLength) {
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(minLength, maxLength);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    @Override
    public Collection<String> generate(final int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> generate())
                .toList();
    }

    @Override
    public Collection<String> generate(int n, int minLength, int maxLength) {
        return IntStream.range(0, n)
                .mapToObj(i -> generate(minLength, maxLength))
                .toList();
    }
}
