import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description: examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments.
 * To check whether the 4 points p, q, r, and s are collinear,
 * check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 ****************************************************************************
 */
public class BruteCollinearPoints {
    private int numOfSegments = 0;
    private List<LineSegment> lineSegments;

    // Throw an IllegalArgumentException if the argument to the constructor is null,
    // if any point in the array is null, or if the argument to the constructor contains a repeated point.
    public BruteCollinearPoints(Point[] points)// finds all line segments containing 4 points
    {
        if (points == null)
            throw new IllegalArgumentException("argument can not contain null values");
        List<Point> pointsList = new ArrayList<Point>(Arrays.asList(points));
        Collections.sort(pointsList);
        duplicatePoints(pointsList);
        nullPoints(points);
        int len = points.length;

        lineSegments = new ArrayList<LineSegment>();
        // increment line segments array
        int x = 0;
        for (int p = 0; p < len; p++) {
            if (points[p] == null)
                throw new IllegalArgumentException("argument can not contain null values");
            for (int q = p + 1; q < len; q++) {
                // null exception
                if (points[q] == null)
                    throw new IllegalArgumentException("argument can not contain null values");
                // repeated point exception
                if (points[q] == points[p])
                    throw new IllegalArgumentException("argument can not contain repeated points");
                for (int r = q + 1; r < len; r++) {
                    if (points[r] == null)
                        throw new IllegalArgumentException("argument can not contain null values");
                    if (points[q] == points[r] || points[r] == points[p])
                        throw new IllegalArgumentException(
                                "argument can not contain repeated points");
                    for (int s = r + 1; s < len; s++) {
                        if (points[s] == points[r] || points[s] == points[q]
                                || points[s] == points[p])
                            throw new IllegalArgumentException(
                                    "argument can not contain repeated points");
                        if (points[s] == null) throw new IllegalArgumentException(
                                "argument can not contain null values");
                        if (points[p].slopeTo(points[q]) == points[q].slopeTo(points[r])
                                && points[q].slopeTo(points[r]) == points[r]
                                .slopeTo(points[s])) {
                            List<Point> sorted = new ArrayList<Point>();
                            sorted.add(0, points[p]);
                            sorted.add(points[q]);
                            sorted.add(points[r]);
                            sorted.add(points[s]);
                            Collections.sort(sorted);
                            numOfSegments++;
                            lineSegments.add(new LineSegment(sorted.get(0), sorted.get(3)));
                            x++;
                        }
                    }
                }
            }
        }
    }

    private void duplicatePoints(List<Point> points) {
        for (int i = 0; i < points.size() - 1; ++i) {
            if (points.get(i).compareTo(points.get(i + 1)) == 0) {
                throw new java.lang.IllegalArgumentException("Duplicated points");
            }
        }
    }

    private void nullPoints(Point[] points) {
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) {
                throw new IllegalArgumentException("At least one point in array is null");
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numOfSegments;
    }

    /*
    this method should include each line segment containing 4 points exactly once.
    If 4 points appear on a line segment in the order p→q→r→s,
    then you should include either the line segment p→s or s→p (but not both)
    and you should not include subsegments such as p→r or q→r. For simplicity,
    we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
    */
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[lineSegments.size()];
        lineSegments.toArray(segments);
        return segments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println(collinear.numOfSegments);
        StdDraw.show();
    }
}
