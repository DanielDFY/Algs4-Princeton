import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStack<Item> implements Iterable<Item> {
    private Item[] s;
    private int N;

    public ArrayStack() {
        s = (Item[]) new Object[2];
        N = 0;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for(int i = 0; i < N; ++i)
            copy[i] = s[i];
        s = copy;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() { return N; }

    public void push(Item item) {
        if(N == s.length)
            resize(2 * N);
        s[N++] = item;
    }

    public Item pop() {
        if(isEmpty()){
            throw new NoSuchElementException("Popping an empty stack!");
        }
        Item item = s[--N];
        s[N] = null;
        if(N > 0 && N == s.length/4)
            resize(s.length/2);
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
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
            return s[--i];
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

