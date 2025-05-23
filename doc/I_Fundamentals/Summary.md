1. It’s the mutable state, stupid.1
All concurrency issues boil down to coordinating access to mutable state. The less mutable state, the easier it is to ensure thread safety.
2. Make fields final unless they need to be mutable.
3. Immutable objects are automatically thread-safe.
Immutable objects simplify concurrent programming tremendously. They are simpler and safer, and can be shared freely without locking or defensive copying.
4. Encapsulation makes it practical to manage the complexity.
You could write a thread-safe program with all data stored in global variables, but why would you want to? Encapsulating data within objects makes it easier to preserve their invariants; encapsulating synchronization within objects makes it easier to comply with their synchronization policy.
5. Guard each mutable variable with a lock.
6. Guard all variables in an invariant with the same lock.
7. Hold locks for the duration of compound actions.
8. A program that accesses a mutable variable from multiple threads without synchronization is a broken program.
9. Don’t rely on clever reasoning about why you don’t need to synchronize.
10. Include thread safety in the design process—or explicitly document that your class is not thread-safe.
11. Document your synchronization policy.