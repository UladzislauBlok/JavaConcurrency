package org.bloku.support.generator;

import java.util.Collection;

public interface Generator<T> {

    T generate();

    T generate(final int min, final int max);

    Collection<T> generate(final int n);

    Collection<T> generate(final int n, final int min, final int max);
}
