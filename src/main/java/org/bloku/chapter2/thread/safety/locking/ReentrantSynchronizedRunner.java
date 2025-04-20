package org.bloku.chapter2.thread.safety.locking;

/**
 * Reentrant locks allow a thread to acquire a lock it already holds, preventing deadlocks. When a thread requests a lock held by another, it blocks, but if it already owns the lock, the request succeeds. The lock tracks an owning thread and an acquisition count, incrementing on re-acquisition and decrementing on exit, releasing the lock when the count reaches zero. This simplifies object-oriented concurrent programming by ensuring that synchronized methods calling superclass methods do not cause deadlocks.
 */
class ReentrantSynchronizedRunner {

    /**
     * This code won't lead to deadlock
     */
    public static void main(final String[] args) {
        Widget widget = new LoggingWidget();
        widget.doSomething();
    }
}

class Widget {
    public synchronized void doSomething() {
        System.out.println("In parent class");
    }
}

class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        System.out.println("In child class");
        super.doSomething();
    }
}
