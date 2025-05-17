Chapter 6. Task Executions
1. Whenever you see code of the form: new Thread(runnable).start() and you think you might at some point want a more flexible execution policy, seriously consider replacing it with the use of an Executor
2. Executor implementations:
   * newFixedThreadPool - A fixed-size thread pool creates threads as tasks are submitted, up to the maximum pool size, and then attempts to keep the pool size constant (adding new threads if a thread dies due to an unexpected Exception).
   * newCachedThreadPool. A cached thread pool has more flexibility to reap idle threads when the current size of the pool exceeds the demand for processing, and to add new threads when demand increases, but places no bounds on the size of the pool.
   * newSingleThreadExecutor. A single-threaded executor creates a single worker thread to process tasks, replacing it if it dies unexpectedly. Tasks are guaranteed to be processed sequentially according to the order imposed by the task queue (FIFO, LIFO, priority order).
   * newScheduledThreadPool. A fixed-size thread pool that supports delayed and periodic task execution, similar to Timer.
3. 
