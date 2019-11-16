package il.co.ilrd.linkedlist;

import java.util.*;

/**
 * Linked List Implementation.
 * @author Matan Keler.
 */
public class LinkedList<E> implements Iterable<E> {
    private Node<E> head = null;
    private int counter = 0;
    private int size = 0;

    /**
     * @return Iterator to the begining of the List.
     */
    @Override
    public Iterator<E> iterator() {
        return (new LinkedListIter());
    }

    /**
     * @return Size of the List.
     */
    public int size() {
        return size;
    }

    /**
     * Prints Lists of every type(that ovrriden toString fucntion).
     * @param list list to print.
     * @throws NullPointerException if list argument is null
     */
    public static void print(LinkedList<?> list) {
        Objects.requireNonNull(list, "list cant be null");
        for (Object obj : list) {
            System.out.println(Objects.toString(obj));
        }
    }

    /**
     * reverse a List.
     * @param list list to reverse.
     * @param <E> generic type.
     * @return LinkedList reference to reversed LinkedList.
     * @throws NullPointerException if list argument is null
     */
    public static <E> LinkedList<E> reverse(LinkedList<E> list) {
        Objects.requireNonNull(list, "list cant be null");

        LinkedList<E> newList = new LinkedList<>();
        for (E elem : list) {
            newList.pushFront(elem);
        }

        return newList;
    }

    /**
     * appends a list.
     * @param list list to append to the current list.
     * @throws NullPointerException if list argument is null
     */
    public void addAll(LinkedList<? extends E> list) {
        boolean isEmptyList = false;
        Objects.requireNonNull(list, "null producer");
        if (list.isEmpty()) { return; }
        if (isEmpty()) { isEmptyList = true; }

        ++counter;
        Node<E> runner = head;
        while (!isEmptyList && null != runner.next) {
            runner = runner.next;
        }

        for (E data : list) {
            ++size;
            if (isEmptyList) {
                runner.next = new Node<E>(data, null);
                isEmptyList = false;
            }
            else {
                runner.next = new Node<E>(data, null);
                runner = runner.next;
            }
        }
    }

    /**
     * @param list List to append current values to.
     * @throws NullPointerException if list argument is null
     */
    public void getAll(LinkedList<? super E> list) {
        boolean isEmptyList = false;
        Objects.requireNonNull(list, "list cant be null");
        if (isEmpty()) { return; }
        if (list.isEmpty()) { isEmptyList = true; }

        ++list.counter;
        Node<? super E> runner = list.head;
        while (!isEmptyList && null != runner.next) {
            runner = runner.next;
        }

        for (E e : this){
            ++list.size;
            if (isEmptyList) {
                runner.next = new Node<>(e, null);
                isEmptyList = false;
            }
            else {
                runner = new Node<>(e, null);
                runner = runner.next;
            }
        }
    }

    /**
     * @return boolean indicate if the List is empty or not.
     */
    public boolean isEmpty() {
        return (null == head);
    }

    /**
     * the find fucntion finds an element otherwise itll return iterator to the dummy.
     * @param data to find in the List.
     * @return Iterator to the found data.
     */
    public Iterator find(E data) {
        Node<E> runner = head;

        while ((null != runner) && (!runner.data.equals(data))) {
            runner = runner.next;
        }

        return (new LinkedListIter(runner));
    }

    /**
     * pop element from the head of the List.
     * @return poped elem value.
     * @throws EmptyListException throws exception if the list is empty
     */
    public E popFront() throws EmptyListException{
        E popedData = null;

        if (isEmpty()) { throw new EmptyListException("Cant pop element from empty list!");}

        ++counter;
        --size;
        popedData = head.data;
        head = head.next;

        return popedData;
    }

    /**
     * push new element to the begin of the List.
     * @param e element to push to the List.
     */
    public void pushFront(E e) {
        ++counter;
        ++size;
        head = new Node<>(e, head);
    }

    /**
     * Generic Node implementation
     * @param <E>
     */
    private static class Node<E> {
        private final E data;
        private Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    /**
     * Generic Iterator for LinkedList Implementation
     */
    private class LinkedListIter implements Iterator<E> {
        private final int expectedCounter = counter;
        private Node<E> iter = head;

        LinkedListIter() {}
        LinkedListIter(Node<E> iter) {
            this.iter = iter;
        }

        /**
         * @return boolean if current iterator has next valid iterator
         */
        @Override
        public boolean hasNext() {
            return (null != iter);
        }

        /**
         * To traverse, goes to next element while returning the value of the last one.
         * @return E generic element(which is the value of the last node).
         * @throws ConcurrentModificationException if the List changed at some point after creating the iterator
         */
        @Override
        public E next() throws ConcurrentModificationException {
            if (counter != expectedCounter) {
                throw new ConcurrentModificationException("chnage have been made");
            }

            E userData = null;
            if (hasNext()) {
                userData = iter.data;
                iter = iter.next;
            }

            return userData;
        }
    }
}
