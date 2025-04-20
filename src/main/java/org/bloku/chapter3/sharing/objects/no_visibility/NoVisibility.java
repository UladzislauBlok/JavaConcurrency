package org.bloku.chapter3.sharing.objects.no_visibility;

class NoVisibility {
    boolean ready;
    int number;

    public void run() {
        new ReaderThread().start();
    }

    private class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }
}
