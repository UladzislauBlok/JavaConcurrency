Chapter 2. Thread safety
1. When designing thread-safe classes, good object-oriented techniques — encapsulation, immutability, and clear specification of invariants—are your best friends.
2. A class is thread-safe if it behaves correctly when accessed from multiple threads, regardless of the scheduling or interleaving of the execution of those threads by the runtime environment, and with no additional synchronization or other coordination on the part of the calling code.
3. Thread-safe classes encapsulate any needed synchronization so that clients need not provide their own.
4. Stateless objects are always thread-safe
5. Operations A and B are atomic with respect to each other if, from the perspective of a thread executing A, when another thread executes B, either all of B has executed or none of it has. An atomic operation is one that is atomic with respect to all operations, including itself, that operate on the same state
6. Where practical, use existing thread-safe objects, like AtomicLong, to manage your class’s state. It is simpler to reason about the possible states and state transitions for existing thread-safe objects than it is for arbitrary state variables, and this makes it easier to maintain and verify thread safety.
7. To preserve state consistency, update related state variables in a single atomic operation.
8. For each mutable state variable that may be accessed by more than one thread, all accesses to that variable must be performed with the same lock held. In this case, we say that the variable is guarded by that lock.
9. Every shared, mutable variable should be guarded by exactly one lock. Make it clear to maintainers which lock that is
10. For every invariant that involves more than one variable, all the variables involved in that invariant must be guarded by the same lock
11. There is frequently a tension between simplicity and performance. When implementing a synchronization policy, resist the temptation to prematurely sacrifice simplicity (potentially compromising safety) for the sake of performance
12. Avoid holding locks during lengthy computations or operations at risk of not completing quickly such as network or console I/O.

Chapter 3. Sharing objects
1. In the absence of synchronization, the compiler, processor, and runtime can do some downright weird things to the order in which operations appear to execute. Attempts to reason about the order in which memory actions “must” happen in insufficiently synchronized multithreaded programs will almost certainly be incorrect.