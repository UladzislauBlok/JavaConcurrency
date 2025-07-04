Chapter 13. Explicit Locks
1. Performance is a moving target; yesterday’s benchmark showing that X is faster than Y may already be out of date today.
2. ReentrantLock is an advanced tool for situations where intrinsic locking is not practical. Use it if you need its advanced features: timed, polled, or interruptible lock acquisition, fair queueing, or non-block-structured locking. Otherwise, prefer synchronized.

Chapter 14. Building Custom Synchronizers
1. Document the condition predicate(s) associated with a condition queue and the operations that wait on them.
2. Every call to wait is implicitly associated with a specific condition predicate. When calling wait regarding a particular condition predicate, the caller must already hold the lock associated with the condition queue, and that lock must also guard the state variables from which the condition predicate is composed.
3. When using condition waits (Object#wait or Condition#await):
   * Always have a condition predicate—some test of object state that must hold before proceeding;
   * Always test the condition predicate before calling wait, and again after returning from wait;
   * Always call wait in a loop;
   * Ensure that the state variables making up the condition predicate are guarded by the lock associated with the condition queue;
   * Hold the lock associated with the condition queue when calling wait, notify, or notifyAll;
   * Do not release the lock after checking the condition predicate but before acting on it.
4. Whenever you wait on a condition, make sure that someone will perform a notification whenever the condition predicate becomes true.
5. Single notify can be used instead of notifyAll only when both of the following conditions hold:
   * Uniform waiters. Only one condition predicate is associated with the condition queue, and each thread executes the same logic upon returning from wait;
   * One-in, one-out. A notification on the condition variable enables at most one thread to proceed.
6. Hazard warning: The equivalents of wait, notify, and notifyAll for Condition objects are await, signal, and signalAll. However, Condition extends Object, which means that it also has wait and notify methods. Be sure to use the proper versions—await and signal—instead!

