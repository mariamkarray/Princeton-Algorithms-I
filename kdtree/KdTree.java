/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private class Node {
        private final Point2D p;
        private Node left;
        private Node right;
        private final RectHV rectangle;  // the rectangle created by this node

        Node(Point2D p, Node left, Node right, RectHV r) {
            this.p = p;
            this.right = right;
            this.left = left;
            this.rectangle = r;
        }
    }

    private int size;
    private Node root;

    public KdTree()   // construct an empty set of points
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty() // is the set empty?
    {
        return (size == 0);
    }

    public int size()   // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p)   // add the point to the set (if it is not already in the set)
    {
        checkNullArgument(p);
        if (!contains(p))
            root = insert(root, null, p, 0, true);
    }

    private Node insert(Node rt, Node parent, Point2D p, int level, boolean even) {
        if (rt == null) {
            RectHV r;
            if (parent == null)
                r = new RectHV(0, 0, 1, 1);
            else {
                // define parents' rectangle dimensions
                double xmin = parent.rectangle.xmin();
                double ymin = parent.rectangle.ymin();
                double xmax = parent.rectangle.xmax();
                double ymax = parent.rectangle.ymax();
                if (even) {
                    if (parent.p.x() > p.x()) xmax = parent.p.x();
                    else xmin = parent.p.x();
                }
                else {
                    if (parent.p.y() > p.y()) ymax = parent.p.y();
                    else ymin = parent.p.y();
                }
                r = new RectHV(xmin, ymin, xmax, ymax);
            }
            rt = new Node(p, null, null, r);
            size++;
            return rt;
        }
        int n = level % 2;
        boolean isEven = false;
        if (n == 0) isEven = true;
        if (isEven) {
            if (p.x() < rt.p.x())
                rt.left = insert(rt.left, rt, p, level + 1, true);
            else if (p.x() >= rt.p.x())
                rt.right = insert(rt.right, rt, p, level + 1, true);
        }
        else {
            if (p.y() < rt.p.y())
                rt.left = insert(rt.left, rt, p, level + 1, false);
            else if (p.y() >= rt.p.y())
                rt.right = insert(rt.right, rt, p, level + 1, false);
        }
        return rt;
    }

    public boolean contains(Point2D p)  // does the set contain point p?
    {
        checkNullArgument(p);
        return contains(root, p, 0);
    }

    private boolean contains(Node rt, Point2D p, int level) {
        if (rt == null) return false;
        if (rt.p.equals(p))
            return true;
        int n = level % 2;
        boolean even = false;
        if (n == 0) even = true;
        if (even) {
            if (p.x() < rt.p.x())
                return contains(rt.left, p, level + 1);
            else if (p.x() >= rt.p.x())
                return contains(rt.right, p, level + 1);
        }
        else {
            if (p.y() < rt.p.y())
                return contains(rt.left, p, level + 1);
            else if (p.y() >= rt.p.y())
                return contains(rt.right, p, level + 1);
        }
        return false;
    }

    public void draw()  // draw all points to standard draw
    {
        if (isEmpty())
            throw new NullPointerException("empty tree");
        // root.rectangle.draw();
        draw(root, true);
    }

    private void draw(Node rt, boolean red) {
        if (rt != null) {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(Color.black);
            rt.p.draw();
            StdDraw.setPenRadius();
            if (red) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(rt.p.x(), rt.rectangle.ymin(), rt.p.x(), rt.rectangle.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rt.rectangle.xmin(), rt.p.y(), rt.rectangle.xmax(), rt.p.y());
            }

            draw(rt.left, !red);
            draw(rt.right, !red);
        }
    }

    private void range(Node rt, RectHV rect, List<Point2D> pointsInsideRectangle) {
        checkNullArgument(rect);
        if (rt.rectangle.intersects(rect)) {
            if (rect.contains(rt.p))
                pointsInsideRectangle.add(rt.p);
        }
        if (rt.right != null)
            range(rt.right, rect, pointsInsideRectangle);
        if (rt.left != null)
            range(rt.left, rect, pointsInsideRectangle);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNullArgument(rect);
        List<Point2D> pointsInsideRectangle = new ArrayList<Point2D>();
        range(root, rect, pointsInsideRectangle);
        return pointsInsideRectangle;
    }


    public Point2D nearest(Point2D p) {
        checkNullArgument(p);
        if (isEmpty())
            return null;
        return (nearest(p, root, root.p));
    }

    private Point2D nearest(Point2D p, Node rt, Point2D nearestPoint) {
        // if the closest point discovered so far is closer than the distance between
        // the query point and the rectangle corresponding to a node,
        // there is no need to explore that node (or its subtrees)
        if (rt == null
                || (nearestPoint != null && nearestPoint.distanceSquaredTo(p) <= rt.rectangle
                .distanceSquaredTo(p)))
            return nearestPoint;
        if (nearestPoint == null
                || rt.p.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p))
            nearestPoint = rt.p;
        if (rt.right != null && rt.right.rectangle.contains(p)) {
            nearestPoint = nearest(p, rt.right, nearestPoint);
            nearestPoint = nearest(p, rt.left, nearestPoint);
        }
        else {
            nearestPoint = nearest(p, rt.right, nearestPoint);
            nearestPoint = nearest(p, rt.left, nearestPoint);
        }
        return nearestPoint;
    }


    private void checkNullArgument(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument can not be null");
    }

    private void checkNullArgument(RectHV r) {
        if (r == null)
            throw new IllegalArgumentException("Argument can not be null");
    }

    // testing
    public static void main(String[] args) {
    }
}
