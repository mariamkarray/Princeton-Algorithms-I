/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private int numOfSegments = 0;
    private List<LineSegment> lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("argument can not contain null values");
        List<Point> pointsList = new ArrayList<Point>(Arrays.asList(points));
        //Collections.sort(pointsList);
        //duplicatePoints(pointsList);
        //nullPoints(points);
        lineSegments = new ArrayList<LineSegment>();
        int len = points.length;
        for (int i = 0; i < len - 1; i++) {
            pointsList.sort(points[i].slopeOrder());
            for (int j = i + 1; j < len - 2; j++) {
                int max = 3;
                Point pp = pointsList.get(i);
                if (pp.slopeTo(pointsList.get(j)) == pp.slopeTo(
                        pointsList.get(j + 1)) && pp.slopeTo(
                        pointsList.get(j)) == pp.slopeTo(pointsList.get(j + 2))) {
                    // line segment found
                    Point lastPoint;

                    List<Point> sorted = new ArrayList<Point>();
                    sorted.add(pp);
                    sorted.add(pointsList.get(j));
                    sorted.add(pointsList.get(j + 1));
                    sorted.add(pointsList.get(j + 2));
                    while ((j + max) < len && pp.slopeTo(pointsList.get(j + 1)) == pp
                            .slopeTo(pointsList.get(j + max))) {
                        lastPoint = pointsList.get(j + max);
                        sorted.add(lastPoint);
                        max++;
                    }
                    Collections.sort(sorted);
                    lineSegments.add(new LineSegment(sorted.get(0), sorted.get(sorted.size() - 1)));
                    numOfSegments++;
                    break;
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
                throw new java.lang.NullPointerException("At least one point in array is null");
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numOfSegments;
    }

    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] segments = new LineSegment[lineSegments.size()];
        lineSegments.toArray(segments);
        return segments;
    }

    public static void main(String[] args) {
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
