package org.bloku.support.generator;

public class GeneratorFactory {
    private GeneratorFactory(){}

    private static final Generator<String> stringGenerator = new StringGenerator();
    private static final Generator<Integer> integerGenerator = new IntegerGenerator();

    public static Generator<String> getStringGenerator() {
        return stringGenerator;
    }

    public static Generator<Integer> getIntegerGenerator() {
        return integerGenerator;
    }
}
