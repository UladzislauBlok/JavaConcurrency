package org.bloku.chapter11.performance.and.scalability;

import org.bloku.support.annotation.GuardedBy;
import org.bloku.support.annotation.ThreadSafe;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class LockSplittingStriping {

    /**
     * ServerStatus shows a portion of the monitoring interface for a database server that maintains the set of currently logged-on users and the set of currently executing queries. As a user logs on or off or query execution begins or ends, the ServerStatus object is updated by calling the appropriate add or remove method.<br>
     * The two types of information are completely independent; ServerStatus could even be split into two separate classes with no loss of functionality
     */
    @ThreadSafe
    private static class ServerStatusUnsplitted {
        @GuardedBy("this")
        public final Set<String> users;
        @GuardedBy("this")
        public final Set<String> queries;

        private ServerStatusUnsplitted(Set<String> users, Set<String> queries) {
            this.users = users;
            this.queries = queries;
        }

        public synchronized void addUser(String u) {
            users.add(u);
        }

        public synchronized void addQuery(String q) {
            queries.add(q);
        }

        public synchronized void removeUser(String u) {
            users.remove(u);
        }

        public synchronized void removeQuery(String q) {
            queries.remove(q);
        }
    }

    /**
     * Instead of guarding both users and queries with the ServerStatus lock, we can instead guard each with a separate lock.<br>
     * After splitting the lock, each new finer-grained lock will see less locking traffic than the original coarser lock would have.<br>
     * If we'll use concurrent collections (e.g. ConcurrentHashMap), we'll both lock splitting (on class level) and stripping (on collections) level
     */
    @ThreadSafe
    private static class ServerStatusSplittedStripped {
        @GuardedBy("users")
        public final Set<String> users;
        @GuardedBy("queries")
        public final Set<String> queries;

        private ServerStatusSplittedStripped(Set<String> users, Set<String> queries) {
            this.users = users;
            this.queries = queries;
        }

        /**
         * Only for example purposes, in this case we don't need synchronized blocks
         */
        private ServerStatusSplittedStripped() {
            this.users = ConcurrentHashMap.newKeySet();
            this.queries = ConcurrentHashMap.newKeySet();
        }

        public void addUser(String u) {
            synchronized (users) {
                users.add(u);
            }
        }

        public void addQuery(String q) {
            synchronized (queries) {
                queries.add(q);
            }
        }

        public void removeUser(String u) {
            synchronized (users) {
                users.remove(u);
            }
        }

        public void removeQuery(String q) {
            synchronized (queries) {
                queries.remove(q);
            }
        }

    }
}
