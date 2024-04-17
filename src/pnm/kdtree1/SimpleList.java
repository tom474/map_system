package pnm.kdtree1;

public class SimpleList {
    private int[][] elements;
    private int count = 0;

    public SimpleList(int initialCapacity) {
        elements = new int[initialCapacity][];
    }

    public void add(int[] element) {
        if (count == elements.length) {
            int[][] newElements = new int[2 * count][];
            System.arraycopy(elements, 0, newElements, 0, count);
            elements = newElements;
        }
        elements[count++] = element;
    }

    public int[][] toArray() {
        int[][] result = new int[count][];
        System.arraycopy(elements, 0, result, 0, count);
        return result;
    }
}
