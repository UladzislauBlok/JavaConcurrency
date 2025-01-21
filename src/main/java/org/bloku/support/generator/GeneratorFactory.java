package org.bloku.support.generator;

public class GeneratorFactory {
    private GeneratorFactory(){}

    private static final Generator<String> stringGenerator = new StringGenerator();

    public static Generator<String> getStringGenerator() {
        return stringGenerator;
    }
}
