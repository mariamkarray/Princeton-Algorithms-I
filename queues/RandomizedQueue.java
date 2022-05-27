import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int numItems;
    private Item[] queue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        numItems = 0;
        queue = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return numItems == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return numItems;
    }

    private void resize(int size) {
        Item[] copy = (Item[]) new Object[size];
        for (int i = 0; i < numItems; i++) {
            copy[i] = queue[i];
        }
        queue = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item cant be null");
        if (numItems == queue.length) resize(2 * queue.length);
        queue[numItems] = item;
        numItems++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        int random = StdRandom.uniform(numItems);
        Item item = queue[random];
        queue[random] = queue[numItems - 1];
        queue[numItems - 1] = null;
        numItems--;
        if (queue.length / 4 == numItems && numItems > 0)
            resize(queue.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        return queue[StdRandom.uniform(numItems)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Item> {
        // array contains random numbers
        private final int[] randNumbers;
        private int j;

        public Itr() {
            j = 0;
            randNumbers = new int[numItems];
            for (int i = 0; i < numItems; i++) {
                randNumbers[i] = i;
            }
            StdRandom.shuffle(randNumbers);
        }

        public boolean hasNext() {
            return (j < numItems);
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("empty deque");
            Item it = queue[randNumbers[j]];
            j++;
            return it;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        StdOut.println(rq.size());
        StdOut.println(rq.isEmpty());
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        StdOut.println("sample: " + rq.sample());
        rq.enqueue(4);
        rq.enqueue(5);
        rq.enqueue(6);
        rq.enqueue(7);
        rq.enqueue(8);
        StdOut.println(rq.dequeue());
        Iterator<Integer> iter = rq.iterator();
        while (iter.hasNext())
            StdOut.println(iter.next());
    }
}
