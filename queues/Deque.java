import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
    }

    private int numItems;
    private Node first; // points to the first element in the deque

    // construct an empty deque
    public Deque() {
        numItems = 0;
        first = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        if (numItems == 0)
            return true;
        return false;
    }

    // return the number of items on the deque
    public int size() {
        return numItems;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item cant be null");
        Node node = new Node();
        numItems++;
        node.item = item;
        if (isEmpty()) {
            first = node;
            return;
        }
        node.next = first;
        first = node;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("item cant be null");
        Node node = new Node();
        numItems++;
        node.item = item;
        Node ptr;
        if (isEmpty()) {
            first = node;
            return;
        }
        ptr = first;
        while (ptr.next != null) {
            ptr = ptr.next;
        }
        ptr.next = node;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        Item val = first.item;
        first = first.next;
        numItems--;
        return val;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        Node ptr = first;
        Item val;
        if (numItems == 1) {
            val = first.item;
            first = null;
            return val;
        }
        while (ptr.next.next != null) {

            ptr = ptr.next;
        }
        val = ptr.next.item;
        ptr.next = null;
        numItems--;
        return val;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Item> {
        private Node curr = first;

        public boolean hasNext() {
            return (curr != null);
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("empty deque");
            Item item = curr.item;
            curr = curr.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<String>();
        StdOut.println(dq.isEmpty());
        StdOut.println(dq.size());

        dq.addFirst("a");
        dq.addFirst("b");
        dq.addLast("c");
        dq.addFirst("d");
        dq.addLast("e");
        dq.removeFirst();
        dq.removeLast();

        Iterator<String> iter = dq.iterator();
        while (iter.hasNext())
            StdOut.println(iter.next());
    }

}
