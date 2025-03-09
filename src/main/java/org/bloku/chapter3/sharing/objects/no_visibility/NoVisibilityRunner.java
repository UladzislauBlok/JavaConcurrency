package org.bloku.chapter3.sharing.objects.no_visibility;

public class NoVisibilityRunner {
    private static final int NUM_OF_RUNS = 10000;

    /**
     * While it may seem obvious that NoVisibility will print 42, it is in fact possible that it will print zero, or never terminate at all! Because it does not use adequate synchronization, there is no guarantee that the values of ready and number written by the main thread will be visible to the reader thread.
     */
    public static void main(String[] args) {
        for (int i = 0; i < NUM_OF_RUNS; i++) {
            NoVisibility noVisibility = new NoVisibility();
            noVisibility.run();
            noVisibility.number = 42;
            noVisibility.ready = true;
        }
    }
}
