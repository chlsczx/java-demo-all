package com.czx.demo.concurrent.nonBlockingStack;

import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> {
    private static class Node<T> {
        final T value;
        Node<T> next;

        Node(final T value) {
            this.value = value;
        }
    }

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    public void push(T value) {
        Node<T> newHead = new Node<>(value);
        Node<T> oldHead;

        do {
            oldHead = head.get();
            newHead.next = oldHead;
        } while (!head.compareAndSet(oldHead, newHead));
    }

    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;

        do {
            oldHead = head.get();
            newHead = null;
            if (oldHead != null)
                newHead = oldHead.next;
        } while (!head.compareAndSet(oldHead, newHead));
        if (oldHead == null) return null;
        return oldHead.value;
    }
}
