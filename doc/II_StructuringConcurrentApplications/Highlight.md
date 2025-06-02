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
7. 