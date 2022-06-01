/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;
    private List<Point2D> pointsList;

    public PointSET() // construct an empty set of points
    {
        points = new TreeSet<Point2D>();
    }

    public boolean isEmpty()   // is the set empty?
    {
        return points.isEmpty();
    }

    public int size()   // number of points in the set
    {
        return points.size();
    }

    public void insert(Point2D p)   // add the point to the set (if it is not already in the set)
    {
        checkNullArgument(p);
        points.add(p);
    }

    public boolean contains(Point2D p) // does the set contain point p?
    {
        checkNullArgument(p);
        return points.contains(p);
    }

    public void draw() // draw all points to standard draw
    {
        pointsList = new ArrayList<Point2D>(points);
        int size = points.size();
        for (int i = 0; i < size; i++) {
            pointsList.get(i).draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        pointsList = new ArrayList<Point2D>(points);
        List<Point2D> pointsInsideRectangle = new ArrayList<Point2D>();
        int size = points.size();
        for (int i = 0; i < size; i++) {
            if (rect.contains(pointsList.get(i)))
                pointsInsideRectangle.add(pointsList.get(i));
        }
        return pointsInsideRectangle;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        pointsList = new ArrayList<Point2D>(points);
        checkNullArgument(p);
        if (points.isEmpty())
            return null;

        double distance = p.distanceTo(pointsList.get(0));
        Point2D nearestPoint = pointsList.get(0);
        int size = points.size();

        for (int i = 1; i < size; i++) {
            if (distance > p.distanceTo(pointsList.get(i))) {
                nearestPoint = pointsList.get(i);
                distance = p.distanceTo(pointsList.get(i));
            }
        }
        return nearestPoint;
    }

    private void checkNullArgument(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument can not be null");
    }

    public static void main(String[] args) {

    }
}
