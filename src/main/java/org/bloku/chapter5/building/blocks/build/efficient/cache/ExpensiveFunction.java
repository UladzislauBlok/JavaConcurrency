package org.bloku.chapter5.building.blocks.build.efficient.cache;

import java.math.BigInteger;

import static org.bloku.support.thread.ThreadUtil.sleepNSeconds;

class ExpensiveFunction implements Computable<String, BigInteger> {

    public BigInteger compute(String arg) {
        sleepNSeconds(5);
        return new BigInteger(arg);
    }
}
