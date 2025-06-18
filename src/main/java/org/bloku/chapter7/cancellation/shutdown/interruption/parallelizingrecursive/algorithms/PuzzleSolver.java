package org.bloku.chapter7.cancellation.shutdown.interruption.parallelizingrecursive.algorithms;

import org.bloku.support.annotation.GuardedBy;
import org.bloku.support.annotation.Immutable;
import org.bloku.support.annotation.ThreadSafe;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

class PuzzleSolver {

    /**
     * Puzzle shows our puzzle abstraction; the type parameters P and M represent the classes for a position and a move. From this interface, we can write a simple sequential solver that searches the puzzle space until a solution is found or the puzzle space is exhausted.
     */
    private interface Puzzle<P, M> {
        P initialPosition();

        boolean isGoal(P position);

        Set<M> legalMoves(P position);

        P move(P position, M move);
    }

    /**
     * Node in represents a position that has been reached through some series of moves, holding a reference to the move that created the position and the previous Node
     */
    @Immutable
    private static class Node<P, M> {
        final P pos;
        final M move;
        final Node<P, M> prev;

        Node(P pos, M move, Node<P, M> prev) {
            this.pos = pos;
            this.move = move;
            this.prev = prev;
        }

        List<M> asMoveList() {
            List<M> solution = new LinkedList<>();
            for (Node<P, M> n = this; n.move != null; n = n.prev)
                solution.addFirst(n.move);
            return solution;
        }
    }

    /**
     * SequentialPuzzleSolver shows a sequential solver for the puzzle framework that performs a depth-first search of the puzzle space. It terminates when it finds a solution (which is not necessarily the shortest solution).
     */
    private static class SequentialPuzzleSolver<P, M> {
        private final Puzzle<P, M> puzzle;
        private final Set<P> seen = new HashSet<>();

        public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
            this.puzzle = puzzle;
        }

        public List<M> solve() {
            P pos = puzzle.initialPosition();
            return search(new Node<>(pos, null, null));
        }

        private List<M> search(Node<P, M> node) {
            if (!seen.contains(node.pos)) {
                seen.add(node.pos);
                if (puzzle.isGoal(node.pos))
                    return node.asMoveList();
                for (M move : puzzle.legalMoves(node.pos)) {
                    P pos = puzzle.move(node.pos, move);
                    Node<P, M> child = new Node<>(pos, move, node);
                    List<M> result = search(child);
                    if (result != null)
                        return result;
                }
            }
            return null;
        }
    }

    /**
     * ConcurrentPuzzleSolver uses an inner SolverTask class that extends Node and implements Runnable. Most of the work is done in run: evaluating the set of possible next positions, pruning positions already searched, evaluating whether success has yet been achieved (by this task or by some other task), and submitting unsearched positions to an Executor.<br>
     * To avoid infinite loops, the sequential version maintained a Set of previously searched positions; ConcurrentPuzzleSolver uses a ConcurrentHashMap for this purpose.
     */
    private static class ConcurrentPuzzleSolver<P, M> {
        private final Puzzle<P, M> puzzle;
        private final ExecutorService exec;
        private final ConcurrentMap<P, Boolean> seen;
        final ValueLatch<Node<P, M>> solution = new ValueLatch<Node<P, M>>();

        private ConcurrentPuzzleSolver(Puzzle<P, M> puzzle, ExecutorService exec, ConcurrentMap<P, Boolean> seen) {
            this.puzzle = puzzle;
            this.exec = exec;
            this.seen = seen;
        }

        public List<M> solve() throws InterruptedException {
            try {
                P p = puzzle.initialPosition();
                exec.execute(newTask(p, null, null));
                // block until solution found
                Node<P, M> solnNode = solution.getValue();
                return (solnNode == null) ? null : solnNode.asMoveList();
            } finally {
                exec.shutdown();
            }
        }

        protected Runnable newTask(P p, M m, Node<P, M> n) {
            return new SolverTask(p, m, n);
        }

        class SolverTask extends Node<P, M> implements Runnable {

            SolverTask(P pos, M move, Node<P, M> prev) {
                super(pos, move, prev);
            }

            public void run() {
                if (solution.isSet() || seen.putIfAbsent(pos, true) != null)
                    return; // already solved or seen this position
                if (puzzle.isGoal(pos))
                    solution.setValue(this);
                else
                    for (M m : puzzle.legalMoves(pos))
                        exec.execute(newTask(puzzle.move(pos, m), m, this));
            }
        }
    }

    /**
     * Each task first consults the solution latch and stops if a solution has already been found. The main thread needs to wait until a solution is found;<br>
     * getValue in ValueLatch blocks until some thread has set the value. ValueLatch provides a way to hold a value such that only the first call actually sets the value, callers can test whether it has been set, and callers can block waiting for it to be set.<br>
     * On the first call to setValue, the solution is updated and the CountDownLatch is decremented, releasing the main solver thread from getValue.
     */
    @ThreadSafe
    private static class ValueLatch<T> {
        @GuardedBy("this")
        private T value = null;
        private final CountDownLatch done = new CountDownLatch(1);

        public boolean isSet() {
            return (done.getCount() == 0);
        }

        public synchronized void setValue(T newValue) {
            if (!isSet()) {
                value = newValue;
                done.countDown();
            }
        }

        public T getValue() throws InterruptedException {
            done.await();
            synchronized (this) {
                return value;
            }
        }
    }
}
