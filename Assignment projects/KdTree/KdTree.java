import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class KdTree {
    private Node root;
    private int count;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Don't insert a null point!");
        root = insert(p, root, true, 0, 0, 1, 1);
    }

    private Node insert(Point2D p, Node node, boolean isEven, double xmin, double ymin, double xmax, double ymax) {
        if (node == null) {
            Node n = new Node(p, new RectHV(xmin, ymin, xmax, ymax));
            count++;
            return n;
        }
        if (node.p.equals(p))
            return node;

        double cmp = compare(node, p, isEven);
        if (cmp < 0) {
            if (isEven)
                node.lb = insert(p, node.lb, false, xmin, ymin, node.p.x(), ymax);
            else
                node.lb = insert(p, node.lb, true, xmin, ymin, xmax, node.p.y());
        }
        else {
            if (isEven)
                node.rt = insert(p, node.rt, false, node.p.x(), ymin, xmax, ymax);
            else
                node.rt = insert(p, node.rt, true, xmin, node.p.y(), xmax, ymax);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Don't search a null point!");
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean isEven) {
        if (node == null)
            return false;
        if (node.p.equals(p))
            return true;

        double cmp = compare(node, p, isEven);
        if (cmp < 0)
            return contains(node.lb, p, !isEven);
        else
            return contains(node.rt, p, !isEven);
    }

    private double compare(Node node, Point2D p, boolean isEven) {
        Comparator<Point2D> cmp = isEven ? Point2D.X_ORDER : Point2D.Y_ORDER;
        return cmp.compare(p, node.p);
    }

    public void draw() {
        if (isEmpty()) return;
        draw(root, true);
    }

    private void draw(Node node, boolean isEven) {
        if (node == null)
            return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.p.draw();
        drawLine(node, isEven);
        draw(node.lb, !isEven);
        draw(node.rt, !isEven);
    }
    private void drawLine(Node node, boolean isEven) {
        if (node == null)
            return;
        StdDraw.setPenRadius();
        if (isEven) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Don't input a null rectangle!");
        if (isEmpty())
            return null;

        Queue<Point2D> queue = new Queue<Point2D>();
        findPoints(root, rect, queue);

        return queue;
    }

    private void findPoints(Node node, RectHV rect, Queue<Point2D> queue) {
        if (rect.contains(node.p))
            queue.enqueue(node.p);
        if (node.lb != null && rect.intersects(node.lb.rect))
            findPoints(node.lb, rect, queue);
        if (node.rt != null && rect.intersects(node.rt.rect))
            findPoints(node.rt, rect, queue);
        return;
    }

    public Point2D nearest(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException("Don't input a null point!");
        if (isEmpty())
            return null;

        return nearest(root, point, null, true);
    }

    private Point2D nearest(Node node, Point2D p, Point2D nearest, boolean isEven) {
        double dist;
        if (nearest == null)
            dist = 3;
        else
            dist = p.distanceSquaredTo(nearest);

        if (p.distanceSquaredTo(node.p) < dist) {
            nearest = node.p;
            dist = p.distanceSquaredTo(nearest);
        }

        boolean leftFirst = isEven ? p.x() < node.p.x() : p.y() < node.p.y();

        if (leftFirst) {
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < dist) {
                nearest = nearest(node.lb, p, nearest, !isEven);
                dist = p.distanceSquaredTo(nearest);
            }
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < dist) {
                nearest = nearest(node.rt, p, nearest, !isEven);
            }
        } else {
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < dist) {
                nearest = nearest(node.rt, p, nearest, !isEven);
                dist = p.distanceSquaredTo(nearest);
            }
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < dist) {
                nearest = nearest(node.lb, p, nearest, !isEven);
            }
        }

        return nearest;
    }
}
