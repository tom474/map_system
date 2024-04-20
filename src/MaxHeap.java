public class MaxHeap {
    private int[][] heap;
    private int size;
    private int maxSize;
    private int targetX;
    private int targetY;

    public MaxHeap(int k, int targetX, int targetY) {
        this.maxSize = k;
        this.heap = new int[k][];
        this.size = 0;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    private int parent(int pos) { return (pos - 1) / 2; }
    private int leftChild(int pos) { return (2 * pos) + 1; }
    private int rightChild(int pos) { return (2 * pos) + 2; }

    private void swap(int fpos, int spos) {
        int[] tmp;
        tmp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = tmp;
    }

    private boolean isLeaf(int pos) { return pos >= (size / 2) && pos < size; }

    long distSquared(int x, int y) {
        long dx = x - targetX;
        long dy = y - targetY;
        return dx * dx + dy * dy;
    }

    private void maxHeapify(int pos) {
        if (!isLeaf(pos)) {
            int left = leftChild(pos);
            int right = rightChild(pos);
            int largest = pos;
            if (left < size && distSquared(heap[left][0], heap[left][1]) > distSquared(heap[pos][0], heap[pos][1])) {
                largest = left;
            }
            if (right < size && distSquared(heap[right][0], heap[right][1]) > distSquared(heap[largest][0], heap[largest][1])) {
                largest = right;
            }
            if (largest != pos) {
                swap(pos, largest);
                maxHeapify(largest);
            }
        }
    }

    public void offer(int[] element) {
        if (size < maxSize) {
            heap[size] = element;
            int current = size;
            while (current > 0 && distSquared(heap[current][0], heap[current][1]) > distSquared(heap[parent(current)][0], heap[parent(current)][1])) {
                swap(current, parent(current));
                current = parent(current);
            }
            size++;
        } else {
            if (distSquared(element[0], element[1]) < distSquared(heap[0][0], heap[0][1])) {
                heap[0] = element;
                maxHeapify(0);
            }
        }
    }

    public int[] poll() {
        int[] popped = heap[0];
        heap[0] = heap[--size];
        maxHeapify(0);
        return popped;
    }

    public int[] peek() {
        return heap[0];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
