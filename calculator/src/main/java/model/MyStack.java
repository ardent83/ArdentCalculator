package model;

public class MyStack<T> {
    public MyStack() {
        this.size = 0;
        Node<T> tail = new Node<>(null);
        head = new Node<>(tail);
    }

    private int size;
    private final Node<T> head;

    private static class Node<T> {
        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public Node(Node<T> next) {
            this.next = next;
        }
        T value;
        Node<T> next;
    }

    public long size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(T t) {
        head.next = new Node<>(t, head.next);
        size++;
    }

    public T pop() {
        if (size == 0)
            throw new ArrayIndexOutOfBoundsException();
        T t = head.next.value;
        head.next = head.next.next;
        size--;
        return t;
    }
    public T peek() {
        if (size == 0)
            throw new ArrayIndexOutOfBoundsException();
        return head.next.value;
    }
}