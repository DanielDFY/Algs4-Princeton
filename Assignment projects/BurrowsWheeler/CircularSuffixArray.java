public class CircularSuffixArray {
    private final char[] chars;
    private final int[] suffixArr;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int from;

        private CircularSuffix(int fromIdx) {
            this.from = fromIdx;
        }

        private char getChar(int fromIdx, int pos) {
            return chars[(fromIdx + pos) % chars.length];
        }

        private int length() {
            return chars.length;
        }

        private char charAt(int d) {
            return this.getChar(from, d);
        }

        public int compareTo(CircularSuffix other) {
            int cmp = 0;
            for (int i = 0; i < chars.length; ++i) {
                char ch1 = getChar(from, i);
                char ch2 = getChar(other.from, i);
                if (ch1 > ch2) {
                    return 1;
                } else if (ch1 < ch2) {
                    return -1;
                }
            }
            return cmp;
        }
    }

    private static class BurrowsMSD {
        private static final int R = 256; // extended ASCII alphabet size
        private static final int CUTOFF = 15; // cutoff to insertion sort

        // do not instantiate
        private BurrowsMSD() { }

        // rearranges the array of extended ASCII strings in ascending order.
        public static void sort(CircularSuffix[] a) {
            int n = a.length;
            CircularSuffix[] aux = new CircularSuffix[n];
            sort(a, 0, n - 1, 0, aux);
        }

        // return dth character of s, -1 if d = length of string
        private static int charAt(CircularSuffix s, int d) {
            assert d >= 0 && d <= s.length();
            if (d == s.length())
                return -1;
            return s.getChar(s.from, d);
        }

        // sort from a[lo] to a[hi], starting at the dth character
        private static void sort(CircularSuffix[] a, int lo, int hi, int d, CircularSuffix[] aux) {

            // cutoff to insertion sort for small sub arrays
            if (hi <= lo + CUTOFF) {
                insertion(a, lo, hi, d);
                return;
            }

            // compute frequency counts
            int[] count = new int[R + 2];
            for (int i = lo; i <= hi; ++i) {
                int c = charAt(a[i], d);
                count[c + 2]++;
            }

            // transform counts to indices
            for (int r = 0; r < R + 1; ++r)
                count[r + 1] += count[r];

            // distribute
            for (int i = lo; i <= hi; ++i) {
                int c = charAt(a[i], d);
                aux[count[c + 1]++] = a[i];
            }

            // copy back
            for (int i = lo; i <= hi; ++i)
                a[i] = aux[i - lo];

            // recursively sort for each character (excludes sentinel -1)
            for (int r = 0; r < R; ++r)
                sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
        }

        // insertion sort a[lo..hi], starting at dth character
        private static void insertion(CircularSuffix[] a, int lo, int hi, int d) {
            for (int i = lo; i <= hi; ++i)
                for (int j = i; j > lo && less(a[j], a[j - 1], d); --j)
                    exchange(a, j, j - 1);
        }

        // exchange a[i] and a[j]
        private static void exchange(CircularSuffix[] a, int i, int j) {
            CircularSuffix temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        // is v less than w, starting at character d
        private static boolean less(CircularSuffix v, CircularSuffix w, int d) {
            // assert v.substring(0, d).equals(w.substring(0, d));
            for (int i = d; i < Math.min(v.length(), w.length()); ++i) {
                if (v.charAt(i) < w.charAt(i))
                    return true;
                if (v.charAt(i) > w.charAt(i))
                    return false;
            }
            return v.length() < w.length();
        }

    }

    public CircularSuffixArray(String s) {
        if (null == s) {
            throw new IllegalArgumentException();
        }

        this.chars = s.toCharArray();

        CircularSuffix[] suffixes = new CircularSuffix[chars.length];
        for (int i = 0; i < chars.length; ++i) {
            suffixes[i] = new CircularSuffix(i);
        }

        BurrowsMSD.sort(suffixes);

        this.suffixArr = new int[chars.length];
        for (int i = 0; i < chars.length; ++i) {
            this.suffixArr[i] = suffixes[i].from;
        }
    } // circular suffix array of s

    public int length() {
        return chars.length;
    } // length of s

    public int index(int i) {
        if (i < 0 || i >= chars.length) {
            throw new IllegalArgumentException();
        }
        return suffixArr[i];
    } // returns index of ith sorted suffix

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); ++i) {
            System.out.println(csa.index(i));
        }
    } // unit testing of the methods (optional)
}