package src;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of an ArrayList, a resizable array structure similar to that found in Java's standard library.
 * This custom ArrayList supports generic elements and implements Iterable for enhanced for-loop compatibility.
 *
 * @param <E> the type of elements in this list
 */
public class ArrayList<E> implements Iterable<E> {
    private static final int DEFAULT_CAPACITY = 10; // Default capacity of the array when initialized
    private Object[] elements; // Array used to store the elements
    private int size; // Number of elements currently in the list

    /**
     * Constructs an empty ArrayList with the default initial capacity.
     */
    public ArrayList() {
        this(DEFAULT_CAPACITY);  // Delegate to the main constructor with default capacity
    }

    /**
     * Constructs an empty ArrayList with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
        size = 0;
    }

    /**
     * Ensures that the underlying array has enough capacity to accommodate additional elements.
     * If the current size equals the array's length, the array is resized to twice its current length.
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     *
     * @param element the element to search for
     * @return the index of the element, or -1 if the element is not found
     */
    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element the element to be appended to this list
     */
    public void add(E element) {
        ensureCapacity();
        elements[size++] = element;
    }

    /**
     * Removes the element at the specified index in this list.
     *
     * @param index the index of the element to be removed
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    public void removeAt(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     *
     * @param element the element to be removed from this list, if present
     * @return true if this list contained the specified element, false otherwise
     */
    public boolean remove(E element) {
        int index = indexOf(element);
        if (index != -1) {
            removeAt(index);
            return true;
        }
        return false;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elements[index];
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index   the index of the element to replace
     * @param element the element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    public E set(int index, E element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all of the elements from this list.
     * The list will be empty after this call returns.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }
    
    /**
     * Returns a new ArrayList containing elements from the specified range within this ArrayList.
     *
     * @param fromIndex the starting index, inclusive
     * @param toIndex the ending index, exclusive
     * @return a new ArrayList containing the elements from the specified range
     * @throws IndexOutOfBoundsException if fromIndex or toIndex are out of range,
     *         or if fromIndex > toIndex.
     */
    public ArrayList<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", Size: " + size);
        }
        ArrayList<E> subList = new ArrayList<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add((E) elements[i]);
        }
        return subList;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0; // Index of the current element
            private boolean removable = false; // Whether remove() is allowed

            /**
             * Checks if there are more elements to iterate over.
             *
             * @return true if there are more elements, false otherwise
             */
            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            /**
             * Retrieves the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if there are no more elements to iterate over
             */
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                removable = true;
                return (E) elements[currentIndex++];
            }

            /**
             * Removes the last element returned by next() from the underlying collection.
             *
             * @throws IllegalStateException if the next method has not yet been called,
             *                               or the remove method has already been called after the last call to the next method
             */
            @Override
            public void remove() {
                if (!removable) {
                    throw new IllegalStateException("Call next() before removing an element");
                }
                ArrayList.this.removeAt(--currentIndex);
                removable = false;
            }
        };
    }
}
