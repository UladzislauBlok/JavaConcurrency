package org.bloku.Chapter16.jmm;

/**
 * PossibleReordering could print (1, 0), or (0, 1), or (1, 1): thread A could run to completion before B starts, B could run to completion before A starts, or their actions could be interleaved.<br>
 * But, strangely, PossibleReordering can also print (0, 0).<br>
 * Without synchronization JVM can reorder operations.
 */
class PossibleReordering {
    static int x = 0, y = 0;
    static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread one = new Thread(() -> {
            a = 1;
            x = b;
        });
        Thread other = new Thread(() -> {
            b = 1;
            y = a;
        });
        one.start();
        other.start();
        one.join();
        other.join();
        System.out.println("( " + x + "," + y + ")");
    }
}
