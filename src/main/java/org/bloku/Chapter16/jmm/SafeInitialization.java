package org.bloku.Chapter16.jmm;

import org.bloku.support.annotation.NotThreadSafe;
import org.bloku.support.annotation.ThreadSafe;

class SafeInitialization {

    // We can init resource twice or see it in incorrect state
    @NotThreadSafe
    private static class UnsafeLazyInitialization {
        private static Resource resource;

        public static Resource getInstance() {
            if (resource == null)
                resource = new Resource(); // unsafe publication
            return resource;
        }
    }

    @ThreadSafe
    private static class SafeLazyInitialization {
        private static Resource resource;

        public synchronized static Resource getInstance() {
            if (resource == null)
                resource = new Resource();
            return resource;
        }
    }

    @ThreadSafe
    private static class EagerInitialization {
        private static Resource resource = new Resource();

        public static Resource getResource() {
            return resource;
        }
    }

    @ThreadSafe
    private static class ResourceFactory {
        private static class ResourceHolder {
            public static Resource resource = new Resource();
        }

        public static Resource getResource() {
            return ResourceHolder.resource;
        }
    }

    // We can see resource in incorrect state, because of reordering
    @NotThreadSafe
    private static class DoubleCheckedLocking {
        private static Resource resource;

        public static Resource getInstance() {
            if (resource == null) {
                synchronized (DoubleCheckedLocking.class) {
                    if (resource == null)
                        resource = new Resource();
                }
            }
            return resource;
        }
    }


    // dummy
    private static class Resource {
    }
}
