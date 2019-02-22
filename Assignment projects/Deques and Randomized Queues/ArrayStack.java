import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStack<Item> implements Iterable<Item> {
    private Item[] a;
    private int N;

    public ArrayStack() {
        a = (Item[]) new Object[2];
        N = 0;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for(int i = 0; i < N; ++i)
            copy[i] = a[i];
        a = copy;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() { return N; }

    public void push(Item item) {
        if(item == null)
            throw new IllegalArgumentException("Input cannot be null!");
        if(N == a.length)
            resize(2 * N);
        a[N++] = item;
    }

    public Item pop() {
        if(isEmpty())
            throw new NoSuchElementException("Pop an empty stack!");
        Item item = a[--N];
        a[N] = null;
        if(N > 0 && N == a.length/4)
            resize(a.length/2);
        return item;
    }

    public Iterator<Item> iterator() {
        return new ReverseListIterator();
    }

    private class ReverseListIterator implements Iterator<Item> {
        private int i = N;

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if(!hasNext())
                throw new NoSuchElementException();
            return a[--i];
        }
    }

    public static void main(String[] args)
    {
        ArrayStack<String> stack = new ArrayStack<>();
        while (!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            if (s.equals("-"))
                StdOut.println(stack.pop());
            else
                stack.push(s);
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}

