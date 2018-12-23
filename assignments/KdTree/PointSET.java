import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> pointSet;

    public PointSET() {
        pointSet = new SET<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Don't insert a null point!");
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Don't search a null point!");
        return pointSet.contains(p);
    }

    public void draw() {
        if (isEmpty()) return;
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Don't input a null rectangle!");
        if (isEmpty())
            return null;

        Queue<Point2D> insidePoints = new Queue<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p))
                insidePoints.enqueue(p);
        }
        return insidePoints;
    }

    public Point2D nearest(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException("Don't input a null point!");
        if (isEmpty())
            return null;

        Point2D nearest = null;
        double min = Double.MAX_VALUE;
        for (Point2D p : pointSet) {
            if (point.distanceSquaredTo(p) < min) {
                nearest = p;
                min = point.distanceSquaredTo(p);
            }
        }
        return nearest;
    }
}
