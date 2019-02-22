import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        if(args.length != 1)
            throw new IllegalArgumentException("Please specify the number of prints!");

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rdmQueue = new RandomizedQueue<>();
        int count = 0;

        while(!StdIn.isEmpty()) {
            String s = StdIn.readString();
            count++;

            // Enqueue until the desired size
            if(count <= k) {
                rdmQueue.enqueue(s);
                continue;
            }

            // Decide whether to replace incoming string with existing members by probability to keep desired size
            if(StdRandom.uniform() <= ((double)k / (double)count)) {
                rdmQueue.dequeue();
                rdmQueue.enqueue(s);
            }
        }

        while(!rdmQueue.isEmpty())
            StdOut.println(rdmQueue.dequeue());
    }
}
