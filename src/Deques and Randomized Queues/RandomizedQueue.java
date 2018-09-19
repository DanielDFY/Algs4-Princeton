import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int N;

    public RandomizedQueue() {
        q = (Item[]) new Object[1];
        N = 0;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for(int i = 0; i < N; ++i)
            copy[i] = q[i];
        q = copy;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void enqueue(Item item) {
        if(item == null)
            throw new IllegalArgumentException("Input cannot be null!");
        if(N == q.length)
            resize(2 * N);
        q[N++] = item;
    }

    public Item dequeue() {
        if(isEmpty())
            throw new NoSuchElementException("Queue is empty!");
        int rdmIdx = StdRandom.uniform(N);
        Item temp = q[rdmIdx];
        q[rdmIdx] = q[--N];
        q[N] = null;
        if(N > 0 && N == q.length/4)
            resize(q.length/2);
        return temp;
    }

    public Item sample() {
        if(isEmpty())
            throw new NoSuchElementException("Queue is empty!");
        return q[StdRandom.uniform(N)];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RdmIterator();
    }

    private class RdmIterator implements Iterator<Item> {
        private int i;
        private int[] rdmIdx;

        private RdmIterator() {
            i = 0;
            rdmIdx = new int[N];
            for(int j = 0; j < N; ++j) {
                rdmIdx[j] = j;
            }
            StdRandom.shuffle(rdmIdx);
        }

        @Override
        public boolean hasNext() {
            return i < N;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if(!hasNext())
                throw new NoSuchElementException();
            return q[rdmIdx[i++]];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rdmQueue = new RandomizedQueue<>();
        for(int i = 0; i < 10; ++i) {
            rdmQueue.enqueue(i);
        }
        StdOut.println("Size: " + rdmQueue.size());
        StdOut.println("Sample: " + rdmQueue.sample());
        for(Integer i : rdmQueue)
            StdOut.println(i);
        while(!rdmQueue.isEmpty())
            StdOut.println(rdmQueue.dequeue());
        StdOut.println("Size: " + rdmQueue.size());
    }
}
