package org.bloku.support.generator;

import java.util.Random;

public class StringGenerator implements Generator<String> {
    private static final String DELIMITER = ",";
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
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(1, CHARS.length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    @Override
    public String generate(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n - 1; i++) {
            sb.append(generate());
            sb.append(DELIMITER);
        }
        sb.append(generate());
        return sb.toString();
    }
}
