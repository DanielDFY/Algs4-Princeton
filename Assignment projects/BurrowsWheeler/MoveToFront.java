import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    private static final int R = 256;
    private static int[] encoding = new int[R];
    private static char[] sequence = new char[R];

    private static void init() {
        for (int i = 0; i < R; ++i) {
            encoding[i] = i;
            sequence[i] = (char) i;
        }
    }

    private static void moveToFront(char c) {
        int current = encoding[c];

        for (int i = current; i > 0; --i) {
            sequence[i] = sequence[i - 1];
            encoding[sequence[i]] = i;
        }
        sequence[0] = c;
        encoding[c] = 0;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        init();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char out = (char) encoding[c];
            BinaryStdOut.write(out);
            moveToFront(c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        init();

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            char c = sequence[i];
            BinaryStdOut.write(c);
            moveToFront(c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            // encode("test.txt"); Debug
            encode();
        else if (args[0].equals("+"))
            decode();
        else throw new IllegalArgumentException("Please use - and + to specify encoding or decoding!");
    }

    // Debug
    private static void dump(Queue<Integer> q) {
        int BYTES_PER_LINE = 16;
        int i;
        for (i = 0; !q.isEmpty(); i++) {
            if (i == 0) StdOut.printf("");
            else if (i % BYTES_PER_LINE == 0) StdOut.printf("\n", i);
            else StdOut.print(" ");
            StdOut.print(Integer.toHexString(q.dequeue()));
        }
        if (BYTES_PER_LINE != 0) StdOut.println();
        StdOut.println((i * 8) + " bits");
    }

    private static void encode(String file) {
        init();
        BinaryIn bin = new BinaryIn(file);
        Queue<Integer> output = new Queue<>();

        while (!bin.isEmpty()) {
            char c = bin.readChar();
            int out = encoding[c];
            output.enqueue(out);
            moveToFront(c);
        }
        dump(output);
    }
}
