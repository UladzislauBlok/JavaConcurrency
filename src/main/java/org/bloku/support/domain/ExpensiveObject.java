package org.bloku.support.domain;

import org.bloku.support.thread.ThreadUtil;

public class ExpensiveObject {
    private static final int MILLISECONDS_NEEDED_FOR_INITIALIZATION = 500;

    public ExpensiveObject() {
        ThreadUtil.sleepNMilliseconds(MILLISECONDS_NEEDED_FOR_INITIALIZATION);
    }
}
