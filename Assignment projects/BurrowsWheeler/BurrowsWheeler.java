import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to
    // standard output
    public static void transform() {
        StringBuilder stringBuilder = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            stringBuilder.append(BinaryStdIn.readChar());
        }
        CircularSuffixArray csa = new CircularSuffixArray(stringBuilder.toString());
        int n = csa.length();
        int m = stringBuilder.length();

        for (int i = 0; i < n; ++i) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < n; ++i) {
            int idx = (csa.index(i) - 1 + m) % m;
            BinaryStdOut.write(stringBuilder.charAt(idx));
        }

        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to
    // standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        int[][] queues = new int[R][2];
        int len = 0;
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int[] q = queues[(int) ch];
            q[0]++;
            if (q[0] >= q.length) {
                int[] nq = new int[q.length * 2];
                System.arraycopy(q, 0, nq, 0, q.length);
                q = nq;
                queues[(int) ch] = q;
            }
            q[q[0]] = len;
            ++len;
        }

        char[] chars = new char[len];
        int[] next = new int[len];
        for (int i = 0, idx = 0; i < R; ++i) {
            int[] q = queues[i];
            if (q[0] > 0) {
                for (int qi = 1; qi <= q[0]; ++qi) {
                    next[idx] = q[qi];
                    chars[idx] = (char) i;
                    ++idx;
                }
            }
        }

        BinaryStdOut.write(chars[first]);
        for (int i = 1, pos = first; i < len; ++i) {
            BinaryStdOut.write(chars[next[pos]]);
            pos = next[pos];
        }

        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Please use - and + to specify encoding or decoding!");
    }
}
