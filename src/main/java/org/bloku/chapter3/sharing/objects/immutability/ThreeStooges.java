package org.bloku.chapter3.sharing.objects.immutability;

import org.bloku.support.annotation.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * Immutable objects can still use mutable objects internally to manage their state, as illustrated by ThreeStooges.
 * <p>While the Set that stores the names is mutable, the design of ThreeStooges makes it impossible to modify that Set after construction.
 * <p>The stooges reference is final, so all object state is reached through a final field.
 */
@Immutable
final class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }
}
