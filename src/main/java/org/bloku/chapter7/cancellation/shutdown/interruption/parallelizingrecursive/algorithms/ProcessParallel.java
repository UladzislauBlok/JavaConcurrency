package org.bloku.chapter7.cancellation.shutdown.interruption.parallelizingrecursive.algorithms;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ProcessParallel {

    private void processSequentially(List<Element> elements) {
        for (Element e : elements)
            process(e);
    }

    /**
     * A call to processInParallel returns more quickly than a call to processSequentially because it returns as soon as all the tasks are queued to the Executor, rather than waiting for them all to complete. If you want to submit a set of tasks and wait for them all to complete, you can use ExecutorService#invokeAll
     */
    private void processInParallel(Executor exec, List<Element> elements) {
        for (final Element e : elements)
            exec.execute(() -> process(e));
    }

    /**
     * The method sequentialRecursive does a depth-first traversal of a tree, performing a calculation on each node and placing the result in a collection.
     */
    private <T> void sequentialRecursive(List<Node<T>> nodes, Collection<T> results) {
        for (Node<T> n : nodes) {
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }

    /**
     * The transformed version, parallelRecursive, also does a depth-first traversal, but instead of computing the result as each node is visited, it submits a task to compute the node result
     */
    private <T> void parallelRecursive(final Executor exec, List<Node<T>> nodes, final Collection<T> results) {
        for (final Node<T> n : nodes) {
            exec.execute(() -> results.add(n.compute()));
            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    /**
     * Callers of parallelRecursive can wait for all the results by creating an Executor specific to the traversal and using shutdown and awaitTermination
     */
    private <T> Collection<T> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

    // dummy
    private void process(Element e) {
    }

    // dummy
    private record Element() {
    }

    // dummy
    private record Node<T>(T value, List<Node<T>> children) {

        List<Node<T>> getChildren() {
            return children;
        }

        public T compute() {
            return null;
        }
    }
}
