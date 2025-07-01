Chapter 10. Avoiding Liveness Hazards
1. A program will be free of lock-ordering deadlocks if all threads acquire the locks they need in a fixed global order.
2. Invoking an alien method with a lock held is asking for liveness trouble. The alien method might acquire other locks (risking deadlock) or block for an unexpectedly long time, stalling other threads that need the lock you hold.
3. Strive to use open calls throughout your program. Programs that rely on open calls are far easier to analyze for deadlock-freedom than those that allow calls to alien methods with locks held.
4. Avoid the temptation to use thread priorities, since they increase platform dependence and can cause liveness problems. Most concurrent applications can use the default priority for all threads.

Chapter 11. Performance and Scalability
1. Scalability describes the ability to improve throughput or capacity when additional computing resources (such as additional CPUs, memory, storage, or I/O bandwidth) are added.
2. Avoid premature optimization. First make it right, then make it fast—if it is not already fast enough.
3. Most performance decisions involve multiple variables and are highly situational. Before deciding that one approach is “faster” than another, ask yourself some questions:
   * What do you mean by “faster”?
   * Under what conditions will this approach actually be faster? Under light or heavy load? With large or small data sets? Can you support your answer with measurements?
   * How often are these conditions likely to arise in your situation? Can you support your answer with measurements?
   * Is this code likely to be used in other situations where the conditions may be different?
   * What hidden costs, such as increased development or maintenance risk, are you trading for this improved performance? Is this a good tradeoff?
4. Measure, don’t guess.
5. All concurrent applications have some sources of serialization; if you think yours does not, look again.
6. Don’t worry excessively about the cost of uncontended synchronization. The basic mechanism is already quite fast, and JVMs can perform additional optimizations that further reduce or eliminate the cost. Instead, focus optimization efforts on areas where lock contention actually occurs.
7. The principal threat to scalability in concurrent applications is the exclusive resource lock
8. There are three ways to reduce lock contention:
   * Reduce the duration for which locks are held;
   * Reduce the frequency with which locks are requested; or
   * Replace exclusive locks with coordination mechanisms that permit greater concurrency.
9. Allocating objects is usually cheaper than synchronizing.

Chapter 12. Testing Concurrent Programs
1. The challenge to constructing effective safety tests for concurrent classes is identifying easily checked properties that will, with high probability, fail if something goes wrong, while at the same time not letting the failure auditing code limit concurrency artificially. It is best if checking the test property does not require any synchronization.
2. Tests should be run on multiprocessor systems to increase the diversity of potential interleaving. However, having more than a few CPUs does not necessarily make tests more effective. To maximize the chance of detecting timing-sensitive data races, there should be more active threads than CPUs, so that at any given time some threads are running and some are switched out, thus reducing the predictability of interactions between threads.
3. Writing effective performance tests requires tricking the optimizer into not optimizing away your benchmark as dead code. This requires every computed result to be used somehow by your program—in a way that does not require synchronization or substantial computation.
4. <b>Inconsistent synchronization.</b><br> Many objects follow the synchronization policy of guarding all variables with the object’s intrinsic lock. If a field is accessed frequently but not always with the 'this' lock held, this may indicate that the synchronization policy is not being consistently followed.<br>
   Analysis tools must guess at the synchronization policy because Java classes do not have formal concurrency specifications. In the future, if annotations such as @GuardedBy are standardized, auditing tools could interpret annotations rather than having to guess at the relationship between variables and locks, thus improving the quality of analysis.
5. <b>Invoking Thread.run.</b><br> Thread implements Runnable and therefore has a run method. However, it is almost always a mistake to call Thread#run directly; usually the programmer meant to call Thread.start.
6. <b>Unreleased lock.</b><br> Unlike intrinsic locks, explicit locks (see Chapter 13) are not automatically released when control exits the scope in which they were acquired. The standard idiom is to release the lock from a 'finally' block; otherwise the lock can remain unreleased in the event of an Exception.
7. <b>Empty synchronized block.</b><br> While empty synchronized blocks do have semantics under the Java Memory Model, they are frequently used incorrectly, and there are usually better solutions to whatever problem the developer was trying to solve.
8. <b>Double-checked locking.</b><br> Double-checked locking is a broken idiom for reducing synchronization overhead in lazy initialization (see Section 16.2.4) that involves reading a shared mutable field without appropriate synchronization.
9. <b>Starting a thread from a constructor.</b><br> Starting a thread from a constructor introduces the risk of subclassing problems, and can allow the 'this' reference to escape the constructor.
10. <b>Notification errors.</b><br> The notify and notifyAll methods indicate that an object’s state may have changed in a way that would unblock threads that are waiting on the associated condition queue. These methods should be called only when the state associated with the condition queue has changed. A synchronized block that calls notify or notifyAll but does not modify any state is likely to be an error. (See Chapter 14.)
11. <b>Condition wait errors.</b><br> When waiting on a condition queue, Object#wait or Condition#await should be called in a loop, with the appropriate lock held, after testing some state predicate (see Chapter 14). Calling Object#wait or Condition#await without the lock held, not in a loop, or without testing some state predicate is almost certainly an error.
12. <b>Misuse of Lock and Condition.</b><br> Using a Lock as the lock argument for a synchronized block is likely to be a typo, as is calling Condition#wait instead of await (though the latter would likely be caught in testing, since it would throw an IllegalMonitorStateException the first time it was called).
13. <b>Sleeping or waiting while holding a lock.</b><br> Calling Thread#sleep with a lock held can prevent other threads from making progress for a long time and is therefore a potentially serious liveness hazard. Calling Object#wait or Condition#await with two locks held poses a similar hazard.
14. <b>Spin loops.<b><br> Code that does nothing but spin (busy wait) checking a field for an expected value can waste CPU time and, if the field is not volatile, is not guaranteed to terminate. Latches or condition waits are often a better technique when waiting for a state transition to occur.