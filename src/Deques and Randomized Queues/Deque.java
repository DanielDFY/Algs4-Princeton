import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node first;
    private Node last;
    private int N;

    public Deque() {
        first = null;
        last = null;
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void addFirst(Item item) {
        if(item == null)
            throw new IllegalArgumentException("Input cannot be null!");
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if(isEmpty())
            last = first;
        else
            first.next.prev = first;
        N++;
    }

    public void addLast(Item item) {
        if(item == null)
            throw new IllegalArgumentException("Input cannot be null!");
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        if(isEmpty())
            first = last;
        else
            last.prev.next = last;
        N++;
    }

    public Item removeFirst() {
        if(isEmpty())
            throw new NoSuchElementException("Removing an empty stack!");
        Item item = first.item;
        N--;
        if(isEmpty()){
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        return item;
    }

    public Item removeLast() {
        if(isEmpty())
            throw new NoSuchElementException("Removing an empty stack!");
        Item item = last.item;
        N--;
        if(isEmpty()){
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if(!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println(deque.isEmpty());
        deque.addFirst(1);
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.isEmpty());
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
            deque.addLast(i * 10);
        }
        StdOut.println(deque.removeLast());
        StdOut.println(deque.size());
        while (!deque.isEmpty()) {
            StdOut.println(deque.removeFirst());
        }
    }
}
