package pnm.kdtree1;

public class SimpleQueue<T> {
    private static class Node<T> {
        T data; // Use generic type T instead of specific type MapNode
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head, tail;

    public SimpleQueue() {
        this.head = this.tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    public T poll() {
        if (isEmpty()) return null;
        T data = head.data;
        head = head.next;
        if (head == null) tail = null;
        return data;
    }
}
