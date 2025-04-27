package org.bloku.chapter5.building.blocks.build.efficient.cache;

interface Computable<IN,OUT> {
    OUT compute(IN arg) throws InterruptedException;
}
