Chapter 6. Task Executions
1. Whenever you see code of the form: new Thread(runnable).start() and you think you might at some point want a more flexible execution policy, seriously consider replacing it with the use of an Executor
2. Executor implementations:
   * newFixedThreadPool - A fixed-size thread pool creates threads as tasks are submitted, up to the maximum pool size, and then attempts to keep the pool size constant (adding new threads if a thread dies due to an unexpected Exception).
   * newCachedThreadPool. A cached thread pool has more flexibility to reap idle threads when the current size of the pool exceeds the demand for processing, and to add new threads when demand increases, but places no bounds on the size of the pool.
   * newSingleThreadExecutor. A single-threaded executor creates a single worker thread to process tasks, replacing it if it dies unexpectedly. Tasks are guaranteed to be processed sequentially according to the order imposed by the task queue (FIFO, LIFO, priority order).
   * newScheduledThreadPool. A fixed-size thread pool that supports delayed and periodic task execution, similar to Timer.
3. The real performance payoff of dividing a program’s workload into tasks comes when there are a large number of independent, homogeneous tasks that can be processed concurrently.

Chapter 7. Cancellation and Shutdown
1. There is nothing in the API or language specification that ties interruption to any specific cancellation semantics, but in practice, using interruption for anything but cancellation is fragile and difficult to sustain in larger applications.
2. Calling interrupt does not necessarily stop the target thread from doing what it is doing; it merely delivers the message that interruption has been requested.
3. Interruption is usually the most sensible way to implement cancellation.
4. Because each thread has its own interruption policy, you should not interrupt a thread unless you know what interruption means to that thread.
5. Only code that implements a thread’s interruption policy may swallow an interruption request. General-purpose task and library code should never swallow interruption requests.
6. When Future#get throws InterruptedException or TimeoutException and you know that the result is no longer needed by the program, cancel the task with Future#cancel.
7. Provide lifecycle methods whenever a thread-owning service has a lifetime longer than that of the method that created it.
8. In long-running applications, always use uncaught exception handlers for all threads that at least log the exception.
9. Daemon threads are not a good substitute for properly managing the lifecycle of services within an application.
10. Avoid finalizers.

Chapter 8. Applying Thread Pools
1. Some tasks have characteristics that require or preclude a specific execution policy. Tasks that depend on other tasks require that the thread pool be large enough that tasks are never queued or rejected; tasks that exploit thread confinement require sequential execution. Document these requirements so that future maintainers do not undermine safety or liveness by substituting an incompatible execution policy.
2. Whenever you submit to an Executor tasks that are not independent, be aware of the possibility of thread starvation deadlock, and document any pool sizing or configuration constraints in the code or configuration file where the Executor is configured.
3. ![pool_size.png](img.png)
4. The newCachedThreadPool factory is a good default choice for an Executor, providing better queuing performance than a fixed thread pool. A fixed size thread pool is a good choice when you need to limit the number of concurrent tasks for resource-management purposes, as in a server application that accepts requests from network clients and would otherwise be vulnerable to overload.
5. Sequential loop iterations are suitable for parallelization when each iteration is independent of the others and the work done in each iteration of the loop body is significant enough to offset the cost of managing a new task.