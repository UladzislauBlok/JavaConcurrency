package org.bloku.chapter3.sharing.objects.escape;

import java.util.HashSet;
import java.util.Set;

/**
 * An object that is published when it should not have been is said to have escaped
 */
class EscapedExamples {

    /**
     * The most blatant form of publication is to store a reference in a public static field, where any class and thread could see it. The initialize method instantiates a new HashSet and publishes it by storing a reference to it into knownSecrets
     * <p>Publishing one object may indirectly publish others. If you add a Secret to the published knownSecrets set, you’ve also published that Secret, because any code can iterate the Set and obtain a reference to the new Secret
     */
    private static class SetExample {
        public static Set<String> knownSecrets;

        public void initialize() {
            knownSecrets = new HashSet<String>();
        }
    }

    /**
     * Returning a reference from a non-private method also publishes the returned object. UnsafeStates publishes the supposedly private array of state abbreviations.
     * <p>Publishing states in this way is problematic because any caller can modify its contents. In this case, the states array has escaped its intended scope, because what was supposed to be private state has been effectively made public
     */
    private static class UnsafeStates {
        private String[] states = new String[]{
                "AK", "AL",
        };

        public String[] getStates() {
            return states;
        }
    }

    /**
     * An alien method is one whose behavior is not fully specified by class itself. This includes methods in other classes as well as overrideable methods (neither private nor final).
     * <p>Passing an object to an alien method must also be considered publishing that object. Since you can’t know what code will actually be invoked, you don’t know that the alien method won’t publish the object or retain a reference to it that might later be used from another thread.
     */
    private static class AlienMethodExample {
        private String state;

        private void initialize() {
            alienMethodInItself("INIT");
        }

        public void alienMethodInItself(String state) {
            // dummy
        }

        private void alienMethodInOther(String state) {
            Other.call(state);
        }
    }
}

// For example proposes
class Other {

    public static void call(String state) {
        // dummy
    }
}
