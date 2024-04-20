/**
 * The List interface defines a general contract for list operations.
 * This interface is designed to be implemented by classes that manage collections of elements in a list format,
 * providing basic functionalities such as add, remove, and access operations.
 *
 * @param <E> the type of elements in this list
 */
public interface List<E> {
    /**
     * Adds an element to the list.
     * This method is intended to append the specified element to the end of the list.
     *
     * @param element the element to be added to the list
     */
    void add(E element);

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     * Returns true if the list contained the specified element.
     *
     * @param element the element to be removed from the list
     * @return true if the element was removed, false otherwise
     */
    boolean remove(E element);

    void removeAt(int index);

    /**
     * Retrieves the element at the specified position in this list.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of the range (index < 0 || index >= size())
     */
    E get(int index);

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    E set(int index, E element);

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in the list
     */
    int size();

    /**
     * Removes all of the elements from this list.
     * The list will be empty after this call returns.
     */
    void clear();
}
