package org.bloku.support.domain;

public interface Processor<IN, OUT> {

    OUT process(IN request);
}
