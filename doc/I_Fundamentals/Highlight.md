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
2. Locking is not just about mutual exclusion; it is also about memory visibility. To ensure that all threads see the most up-to-date values of shared mutable variables, the reading and writing threads must synchronize on a common lock.
3. Use volatile variables only when they simplify implementing and verifying your synchronization policy; avoid using volatile variables when verifying correctness would require subtle reasoning about visibility. Good uses of volatile variables include ensuring the visibility of their own state, that of the object they refer to, or indicating that an important lifecycle event (such as initialization or shutdown) has occurred.
4. Locking can guarantee both visibility and atomicity; volatile variables can only guarantee visibility.
5. Do not allow the 'this' reference to escape during construction
6. Immutable objects are always thread-safe.
7. An object is immutable if:
   * Its state cannot be modified after construction;
   * All its fields are final;
   * It is properly constructed (the 'this' reference does not escape during construction).
8. Just as it is a good practice to make all fields private unless they need greater visibility, it is a good practice to make all fields final unless they need to be mutable.
9. Immutable objects can be used safely by any thread without additional synchronization, even when synchronization is not used to publish them.
10. To publish an object safely, both the reference to the object and the object’s state must be made visible to other threads at the same time. A properly constructed object can be safely published by:
    * Initializing an object reference from a static initializer;
    * Storing a reference to it into a volatile field or AtomicReference;
    * Storing a reference to it into a final field of a properly constructed object;
    * Storing a reference to it into a field that is properly guarded by a lock.
11. Collection rules:
    * Placing a key or value in a Hashtable, synchronizedMap, or ConcurrentMap safely publishes it to any thread that retrieves it from the Map (whether directly or via an iterator);
    * Placing an element in a Vector, CopyOnWriteArrayList, CopyOnWriteArraySet, synchronizedList, or synchronizedSet safely publishes it to any thread that retrieves it from the collection;
    * Placing an element on a BlockingQueue or a ConcurrentLinkedQueue safely publishes it to any thread that retrieves it from the queue.
12. Safely published effectively immutable objects can be used safely by any thread without additional synchronization.
13. The publication requirements for an object depend on its mutability:
    * Immutable objects can be published through any mechanism;
    * Effectively immutable objects must be safely published;
    * Mutable objects must be safely published, and must be either thread safe or guarded by a lock.
14. The most useful policies for using and sharing objects in a concurrent program are:
    * Thread-confined. A thread-confined object is owned exclusively by and confined to one thread, and can be modified by its owning thread.
    *Shared read-only. A shared read-only object can be accessed concurrently by multiple threads without additional synchronization, but cannot be modified by any thread. Shared read-only objects include immutable and effectively immutable objects.
    * Shared thread-safe. A thread-safe object performs synchronization internally, so multiple threads can freely access it through its public interface without further synchronization.
    *Guarded. A guarded object can be accessed only with a specific lock held. Guarded objects include those that are encapsulated within other thread-safe objects and published objects that are known to be guarded by a specific lock.

Chapter 4. Composing objects
1. The design process for a thread-safe class should include these three basic elements:
   * Identify the variables that form the object’s state;
   * Identify the invariants that constrain the state variables;
   * Establish a policy for managing concurrent access to the object’s state.
2. You cannot ensure thread safety without understanding an object’s invariants and post conditions. Constraints on the valid values or state transitions for state variables can create atomicity and encapsulation requirements.
3. Encapsulating data within an object confines access to the data to the object’s methods, making it easier to ensure that the data is always accessed with the appropriate lock held.
4. Confinement makes it easier to build thread-safe classes because a class that confines its state can be analyzed for thread safety without having to examine the whole program.
5. If a class is composed of multiple independent thread-safe state variables and has no operations that have any invalid state transitions, then it can delegate thread safety to the underlying state variables
6. If a state variable is thread-safe, does not participate in any invariants that constrain its value, and has no prohibited state transitions for any of its operations, then it can safely be published.
7. Document a class’s thread safety guarantees for its clients; document its synchronization policy for its maintainers.

Chapter 5. Building Blocks
1. Just as encapsulating an object’s state makes it easier to preserve its invariants, encapsulating its synchronization makes it easier to enforce its synchronization policy.
2. Replacing synchronized collections with concurrent collections can offer dramatic scalability improvements with little risk.
3. Bounded queues are a powerful resource management tool for building reliable applications: they make your program more robust to overload by throttling activities that threaten to produce more work than can be handled.