import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {     // finds all line segments containing 4 or more points
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
        checkSameFast(sortedPoints);

        for (int i = 0; i < sortedPoints.length - 3; ++i) {
            Arrays.sort(sortedPoints, i, sortedPoints.length);
            Point origin = sortedPoints[i];
            Arrays.sort(sortedPoints, i + 1, sortedPoints.length, origin.slopeOrder());

            int count = 0;
            double preSlope = Double.NaN;
            Point prePoint = origin;

            for (int j = i + 1; j < sortedPoints.length; ++j) {
                Point point = sortedPoints[j];
                double slope = origin.slopeTo(point);

                if (Double.compare(slope, preSlope) == 0) {
                    count++;
                } else {
                    if (count >= 3) {
                        lineSegments.add(new LineSegment(origin, prePoint));
                    }
                    count = 1;
                    preSlope = slope;
                }

                prePoint = point;
            }

            if (count >= 3) {
                lineSegments.add(new LineSegment(origin, prePoint));
            }
        }
    }

    private void checkSameFast(Point[] sortedPoints) {
        for (int i = 0; i < sortedPoints.length - 1; ++i) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("Same points exist!");
            }
        }
    }

    public int numberOfSegments() {                 // the number of line segments
        return lineSegments.size();
    }

    public LineSegment[] segments() {               // the line segments
        LineSegment[] lines = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegments.size(); ++i) {
            lines[i] = lineSegments.get(i);
        }
        return lines;
    }
}
