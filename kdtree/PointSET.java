import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointsSet;

    public PointSET() {
        pointsSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return pointsSet.isEmpty();
    }

    public int size() {
        return pointsSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointsSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointsSet.contains(p);
    }

    public void draw() {
        for (Point2D point : pointsSet) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> result = new ArrayList<>();
        for (Point2D point : pointsSet) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearest = null;
        for (Point2D point : pointsSet) {
            if (nearest == null || p.distanceSquaredTo(point) < p.distanceSquaredTo(nearest)) {
                nearest = point;
            }
        }
        return nearest;
    }
}