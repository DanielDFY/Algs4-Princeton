import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Comparator;
import java.util.TreeSet;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p) {
            this.p = p;
        }
    }

    private Node root;
    private int N;

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Don't insert a null point!");
        root = insert(p, root, 1, new RectHV(0, 0, 1, 1));
        N++;
    }

    private Node insert(Point2D p, Node node, int level, RectHV rect) {
        if (node == null) {
            Node n = new Node(p);
            n.rect = rect;
            return n;
        }

    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Don't search a null point!");
        return contains(root, p, 0);
    }

    private boolean contains(Node node, Point2D p, int level) {
        if (node == null)
            return false;
        int cmp = compare(node, p, level);
        if (cmp < 0)
            return contains(node.lb, p, level + 1);
        else if (cmp > 0)
            return contains(node.rt, p, level + 1);
        return true;
    }

    private int compare(Node node, Point2D p, int level) {
        Comparator<Point2D> cmp = level % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
        return cmp.compare(p, node.p);
    }

    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {

    }

    public Point2D nearest(Point2D p) {

    }
}
