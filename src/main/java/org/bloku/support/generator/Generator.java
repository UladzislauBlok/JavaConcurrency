package org.bloku.support.generator;

public interface Generator<T> {

    T generate();

    T generate(final int n);
}
