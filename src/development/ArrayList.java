package development;

/**
 * A simple implementation of a list using a dynamic array to store elements.
 * This custom ArrayList class mimics some of the functionalities of the Java Collection Framework's ArrayList,
 * but is simplified for educational purposes.
 *
 * @param <E> the type of elements in this list
 */
public class ArrayList<E> {
    private Object[] elements;  // The array buffer into which the elements of the ArrayList are stored.
    private int size = 0;       // The current number of elements contained in the ArrayList.
    private static final int DEFAULT_CAPACITY = 10; // Default initial capacity of the ArrayList.

    // Constructs an empty list with an initial capacity of ten.
    public ArrayList() {
        elements = new Place[DEFAULT_CAPACITY];
    }

    /**
     * Ensures that the array has the capacity to add new elements. If the current size equals
     * the capacity of the array, the array is resized to double its current size.
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] temp = new Object[elements.length * 2];
            System.arraycopy(elements, 0, temp, 0, size);   // Copy existing elements to the new array
            elements = temp;
        }
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element element to be appended to this list
     */
    public void add(E element) {
        // Ensure there's enough space for the new element
        ensureCapacity();
        // Add the element and increment the size
        elements[size] = element;
        size++;
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     * If the list does not contain the element, it remains unchanged.
     *
     * @param element element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public boolean remove(E element) {
        int index = indexOf(element);
        if (index != -1) {
            return removeAt(index);
        }
        return false;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     */
    public boolean removeAt(int index) {
        if (index < 0 || index >= size) {
            return false;
        }
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        return true;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elements[index];
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            return null;
        }
        E oldValue = (E) elements[index];   // Cast needed because elements is an Object array
        elements[index] = element;
        return oldValue;
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     *
     * @param element element to search for
     * @return the index of the first occurrence of the specified element in this list,
     *         or -1 if this list does not contain the element
     */
    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes all the elements from this list.
     * The list will be empty after this call returns.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }
}
