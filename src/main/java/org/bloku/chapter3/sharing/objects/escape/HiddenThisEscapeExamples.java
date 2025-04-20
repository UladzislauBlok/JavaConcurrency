package org.bloku.chapter3.sharing.objects.escape;

class HiddenThisEscapeExamples {

    /**
     * When HiddenThisEscape publishes the EventListener, it implicitly publishes the enclosing HiddenThisEscape instance as well, because inner class instances contain a hidden reference to the enclosing instance.
     * <p>An object is in a predictable, consistent state only after its constructor returns, so publishing an object from within its constructor can publish an incompletely constructed object. This is true even if the publication is the last statement in the constructor. If the 'this' reference escapes during construction, the object is considered not properly constructed.
     * <p>Calling an overrideable instance method (one that is neither private nor final) from the constructor can also allow the 'this' reference to escape.
     */
    static class HiddenThisEscape {

        public HiddenThisEscape(EventSource source) {
            source.registerListener(
                    new HiddenThisEscapeExamples.EventListener() {
                        public void onEvent(EventSource.Event e) {
                            // dummy
                        }
                    });
        }

    }

    /**
     * If you are tempted to register an event listener or start a thread from a constructor, you can avoid the improper construction by using a private constructor and a public factory method.
     */
    public static class SafeListener {

        private final EventListener listener;

        private SafeListener() {
            listener = new EventListener() {
                public void onEvent(EventSource.Event e) {
                    // dummy
                }
            };
        }

        public static SafeListener newInstance(EventSource source) {
            SafeListener safe = new SafeListener();
            source.registerListener(safe.listener);
            return safe;
        }
    }

    public static class EventListener {

    }
}

// For example proposes
class EventSource {


    public void registerListener(HiddenThisEscapeExamples.EventListener listener) {
        // dummy
    }

    public static class Event {

    }
}
