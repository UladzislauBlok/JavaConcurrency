package org.bloku.Chapter15.atomic.variables.nonblocking.synchronization;

import org.bloku.support.annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ConcurrentStack shows how to construct a stack using atomic references.<br>
 * The stack is a linked list of Node elements, rooted at top, each of which contains a value and a link to the next element.<br>
 * The push method prepares a new link node whose next field refers to the current top of the stack, and then uses CAS to try to install it on the top of the stack.
 */
@ThreadSafe
class ConcurrentStack<E> {
    AtomicReference<Node<E>> top = new AtomicReference<>();

    public void push(E item) {
        Node<E> newHead = new Node<>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    private static class Node<E> {
        public final E item;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
}

