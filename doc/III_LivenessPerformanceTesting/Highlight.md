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
3. 