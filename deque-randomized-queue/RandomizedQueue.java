import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size = 0;

    public RandomizedQueue() {
        items = (Item[]) new Object[10];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        items[size] = item;
        size++;
        if (size == items.length) {
            resize(items.length * 2);
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int randomIndex;

        if (size == 1) {
            randomIndex = 0;
        }
        else {
            randomIndex = StdRandom.uniform(size);
        }

        Item itemToReturn = items[randomIndex];
        items[randomIndex] = items[size-1];
        items[size-1] = null;
        size--;

        if (size <= items.length / 4) {
            resize(items.length / 2);
        }

        return itemToReturn;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return items[StdRandom.uniform(size)];
    }

    private void resize(int newLength) {
        Item[] newItems = (Item[]) new Object[newLength];
        for (int j = 0; j < newLength; j++) {
            if (j < items.length) {
                newItems[j] = items[j];
            }
            else {
                newItems[j] = null;
            }
        }
        items = newItems;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final int[] randomIndexes;
        private int currentRandomIndex = 0;

        public RandomizedQueueIterator() {
            randomIndexes = StdRandom.permutation(size);
        }

        @Override
        public boolean hasNext() {
            return currentRandomIndex < randomIndexes.length;
        }

        @Override
        public Item next() {
            if (currentRandomIndex >= randomIndexes.length) {
                throw new NoSuchElementException();
            }
            Item itemToReturn = items[randomIndexes[currentRandomIndex]];
            currentRandomIndex++;
            return itemToReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> stringRandomizedQueue = new RandomizedQueue<>();
        stringRandomizedQueue.enqueue("Test1");
        StdOut.println(stringRandomizedQueue.dequeue());
        stringRandomizedQueue.enqueue("TEst2");
        stringRandomizedQueue.enqueue("Test1");
        StdOut.println(stringRandomizedQueue.dequeue());
        StdOut.println(stringRandomizedQueue.dequeue());



        StdOut.println(String.format("Queue size is %d empty status is %s", stringRandomizedQueue.size(), stringRandomizedQueue.isEmpty()));
    }
}
