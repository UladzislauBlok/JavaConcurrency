package org.bloku.chapter3.sharing.objects.thread_confinement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

class ConfinementExamples {

    /**
     * Ad-hoc thread confinement describes when the responsibility for maintaining thread confinement falls entirely on the implementation.
     * <p>It is safe to perform read-modify-write operations on shared volatile variables as long as you ensure that the volatile variable is only written from a single thread. In this case, you are confining the modification to a single thread to prevent race conditions, and the visibility guarantees for volatile variables ensure that other threads see the most up-to-date value.
     */
    private static class AdHocThreadConfinement {
        private volatile int counter;

        /**
         * It's safe if it's called from only one thread
         */
        public void count() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }

    /**
     * Stack confinement is a special case of thread confinement in which an object can only be reached through local variables.
     * <p>Local variables are intrinsically confined to the executing thread; they exist on the executing thread’s stack, which is not accessible to other threads.
     * <p>For primitively typed local variables, such as counter in loadString, you cannot violate stack confinement even if you tried. There is no way to obtain a reference to a primitive variable, so the language semantics ensure that primitive local variables are always stack confined.
     */
    private static class StackConfinement {
        private ThreadSafeStringLoader loader;


        public int loadString(Collection<String> strings) {
            SortedSet<String> stringsSet;
            int counter = 0;
            String candidate = null;
            stringsSet = new TreeSet<>(Comparator.naturalOrder());
            stringsSet.addAll(strings);
            for (String str : stringsSet) {
                if (candidate == null || candidate.startsWith("https"))
                    candidate = str;
                else {
                    loader.loadString(str);
                    ++counter;
                    candidate = null;
                }
            }
            return counter;
        }
    }

    /**
     * ThreadLocal provides get and set accessor methods that maintain a separate copy of the value for each thread that uses it, so a get returns the most recent value passed to set from the currently executing thread.
     * <p>If you are porting a single-threaded application to a multithreaded environment, you can preserve thread safety by converting shared global variables into ThreadLocals, if the semantics of the shared globals permits this;
     */
    private static class ThreadLocalConfinement {
        private static ThreadLocal<Connection> connectionHolder
                = ThreadLocal.withInitial(() -> {
                    try {
                        return DriverManager.getConnection("DB_URL");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        public static Connection getConnection() {
            return connectionHolder.get();
        }
    }
}

class ThreadSafeStringLoader {

    public synchronized void loadString(String string) {
        // dummy
    }
}
