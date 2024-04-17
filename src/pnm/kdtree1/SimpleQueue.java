package pnm.kdtree1;

public class SimpleQueue {
    private static class Node {
        KDNode kdNode;
        Node next;

        Node(KDNode kdNode) {
            this.kdNode = kdNode;
            this.next = null;
        }
    }

    private Node head, tail;

    public SimpleQueue() {
        this.head = this.tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void add(KDNode kdNode) {
        Node newNode = new Node(kdNode);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    public KDNode poll() {
        if (isEmpty()) return null;
        KDNode kdNode = head.kdNode;
        head = head.next;
        if (head == null) tail = null;
        return kdNode;
    }
}
