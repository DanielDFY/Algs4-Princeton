import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {               // finds all line segments containing 4 points

        if (points == null) {
            throw new IllegalArgumentException("Null argument!");
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Null point!");
            }
        }

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkSameBrute(sortedPoints);

        for (int i = 0; i < sortedPoints.length - 3; ++i) {
            Point p1 = sortedPoints[i];
            for (int j = i + 1; j < sortedPoints.length - 2; ++j) {
                Point p2 = sortedPoints[j];
                for (int k = j + 1; k < sortedPoints.length - 1; ++k) {
                    Point p3 = sortedPoints[k];

                    if (!isCollinear(p1, p2, p3))
                        continue;

                    for (int m = k + 1; m < sortedPoints.length; ++m) {
                        Point p4 = sortedPoints[m];

                        if (isCollinear(p2, p3, p4)) {
                            lineSegments.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }
    }

    private void checkSameBrute(Point[] sortedPoints) {
        for (int i = 0; i < sortedPoints.length - 1; ++i) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("Same points exist!");
            }
        }
    }

    private boolean isCollinear(Point p1, Point p2, Point p3) {
        double slope1 = p1.slopeTo(p2);
        double slope2 = p2.slopeTo(p3);

        return Double.compare(slope1, slope2) == 0;
    }

    public int numberOfSegments() {                             // the number of line segments
        return lineSegments.size();
    }

    public LineSegment[] segments() {                           // the line segments
        LineSegment[] lines = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegments.size(); ++i) {
            lines[i] = lineSegments.get(i);
        }
        return lines;
    }
}
