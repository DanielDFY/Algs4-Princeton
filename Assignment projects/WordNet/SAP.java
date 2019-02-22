import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph digraph;

    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        digraph = new Digraph(G);
    }

    public int length(int v, int w) {
        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        return pathFinder(vPath, wPath);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        return pathFinder(vPath, wPath);
    }

    public int ancestor(int v, int w) {
        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        return ancestorFinder(vPath, wPath);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        return ancestorFinder(vPath, wPath);
    }

    private void validate(int v) {
        if (v < 0 || v > digraph.V() - 1) throw new IllegalArgumentException();
    }

    private void validate(Iterable<Integer> v) {
        for (Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException();
            validate(i);
        }
    }

    private int pathFinder(BreadthFirstDirectedPaths vPath, BreadthFirstDirectedPaths wPath) {
        int min = INFINITY;
        for (int i = 0; i < digraph.V(); ++i) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int len = vPath.distTo(i) + wPath.distTo(i);
                if (len < min) {
                    min = len;
                }
            }
        }
        return min < INFINITY ? min : -1;
    }

    private int ancestorFinder(BreadthFirstDirectedPaths vPath, BreadthFirstDirectedPaths wPath) {
        int min = INFINITY;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); ++i) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int len = vPath.distTo(i) + wPath.distTo(i);
                if (len < min) {
                    min = len;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
