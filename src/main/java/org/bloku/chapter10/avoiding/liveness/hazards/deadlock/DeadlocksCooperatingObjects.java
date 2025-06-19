package org.bloku.chapter10.avoiding.liveness.hazards.deadlock;

import org.bloku.support.annotation.GuardedBy;
import org.bloku.support.annotation.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * While no method explicitly acquires two locks, callers of setLocation and getImage can acquire two locks just the same. If a thread calls setLocation in response to an update from a GPS receiver, it first updates the taxi’s location and then checks to see if it has reached its destination.<br>
 * If it has, it informs the dispatcher that it needs a new destination. Since both setLocation and notifyAvailable are synchronized, the thread calling setLocation acquires the Taxi lock and then the Dispatcher lock. Similarly, a thread calling getImage acquires the Dispatcher lock and then each Taxi lock (one at time).
 */
class DeadlocksCooperatingObjects {

    private static class Unsafe {
        private static class Taxi {
            @GuardedBy("this")
            private Point location, destination;
            private final Dispatcher dispatcher;

            public Taxi(Dispatcher dispatcher) {
                this.dispatcher = dispatcher;
            }

            public synchronized Point getLocation() {
                return location;
            }

            public synchronized void setLocation(Point location) {
                this.location = location;
                if (location.equals(destination))
                    dispatcher.notifyAvailable(this);
            }
        }

        private static class Dispatcher {
            @GuardedBy("this")
            private final Set<Taxi> taxis;
            @GuardedBy("this")
            private final Set<Taxi> availableTaxis;

            public Dispatcher() {
                taxis = new HashSet<>();
                availableTaxis = new HashSet<>();
            }

            public synchronized void notifyAvailable(Taxi taxi) {
                availableTaxis.add(taxi);
            }

            public synchronized Image getImage() {
                Image image = new Image();
                for (Taxi t : taxis)
                    image.drawMarker(t.getLocation());
                return image;
            }
        }
    }

    /**
     * Very often, the cause of problems like those in Unsafe is the use of synchronized methods instead of smaller synchronized blocks for reasons of compact syntax or simplicity rather than because the entire method must be guarded by a lock.
     */
    private static class Safe {
        class Taxi {
            @GuardedBy("this")
            private Point location, destination;
            private final Dispatcher dispatcher;

            Taxi(Dispatcher dispatcher) {
                this.dispatcher = dispatcher;
            }

            public synchronized Point getLocation() {
                return location;
            }

            public void setLocation(Point location) {
                boolean reachedDestination;
                synchronized (this) {
                    this.location = location;
                    reachedDestination = location.equals(destination);
                }
                if (reachedDestination)
                    dispatcher.notifyAvailable(this);
            }
        }

        @ThreadSafe
        class Dispatcher {
            @GuardedBy("this")
            private final Set<Taxi> taxis;
            @GuardedBy("this")
            private final Set<Taxi> availableTaxis;

            public Dispatcher() {
                taxis = new HashSet<>();
                availableTaxis = new HashSet<>();
            }

            public synchronized void notifyAvailable(Taxi taxi) {
                availableTaxis.add(taxi);
            }

            public Image getImage() {
                Set<Taxi> copy;
                synchronized (this) {
                    copy = new HashSet<>(taxis);
                }
                Image image = new Image();
                for (Taxi t : copy)
                    image.drawMarker(t.getLocation());
                return image;
            }
        }
    }

    // dummy
    private record Point() {
    }

    // dummy
    private record Image() {
        public void drawMarker(Point location) {
        }
    }
}
/*
ThreadSafe

 */
