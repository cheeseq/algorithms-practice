import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    public Deque() {
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node oldFisrt = first;
        Node newNode = new Node(item, oldFisrt, null);
        first = newNode;
        size++;
        if (size == 1) {
            last = newNode;
        }
        if (size > 1) {
            oldFisrt.setPrev(newNode);
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node oldLast = last;
        Node newNode = new Node(item, null, oldLast);
        last = newNode;
        size++;
        if (size == 1) {
            first = newNode;
        }
        if (size > 1) {
            oldLast.setNext(newNode);
        }
    }

    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Item itemToRemove = first.getValue();

        if (size > 1) {
            first = first.getNext();
            first.setPrev(null);
        } else {
            first = null;
            last = null;
        }

        size--;
        return itemToRemove;
    }

    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Item itemToRemove = last.getValue();

        if (size > 1) {
            last = last.getPrev();
            last.setNext(null);
        } else {
            first = null;
            last = null;
        }

        size--;
        return itemToRemove;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    private class DequeIterator implements Iterator<Item> {
        private Node next;

        public DequeIterator(Node next) {
            this.next = next;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Item next() {
            if (next == null || size == 0) {
                throw new NoSuchElementException();
            }
            Node currentToReturn = next;
            next = next.getNext();
            return currentToReturn.getValue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private Node prev;
        private Node next;
        private final Item value;

        public Node(Item value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        public Item getValue() {
            return value;
        }

        public Node getPrev() {
            return prev;
        }

        public Node getNext() {
            return next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
        Deque<String> anotherDeque = new Deque<>();
        anotherDeque.addFirst("test");
        StdOut.println(anotherDeque.removeLast());
        anotherDeque.addLast("test2");
        anotherDeque.addFirst("test3");
        StdOut.println(anotherDeque.removeFirst());
        StdOut.println(anotherDeque.removeLast());

        Deque<String> deque = new Deque<>();
        deque.addLast("LAST");
        deque.addFirst("MIDDLE");
        deque.addLast("VERY LAST");
        deque.addFirst("FIRST");

        StdOut.println(String.format("Added %d elements with addLast and addFirst", deque.size()));
        for (String str : deque) {
            StdOut.println(str);
        }

        String deleted;
        deleted = deque.removeLast();
        StdOut.println(String.format("Remove last test, deleted item is %s, size is %d, isEmpty status is %s", deleted, deque.size(), deque.isEmpty()));

        deleted = deque.removeFirst();
        StdOut.println(String.format("Remove first test, deleted item is %s, size is %d, isEmpty status is %s", deleted, deque.size(), deque.isEmpty()));

        deleted = deque.removeFirst();
        StdOut.println(String.format("Remove first test, deleted item is %s, size is %d, isEmpty status is %s", deleted, deque.size(), deque.isEmpty()));

        deleted = deque.removeLast();
        StdOut.println(String.format("Remove last test, deleted item is %s, size is %d, isEmpty status is %s", deleted, deque.size(), deque.isEmpty()));
    }
}