import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size = 0;
    private Point2D nearest;

    public KdTree() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        root = put(root, p, true, new RectHolder(0.0, 0.0, 1.0, 1.0));
    }

    private Node put(Node node, Point2D point, boolean compareByX, RectHolder boundRect) {
        if (node == null) {
            size++;
            RectHV rectHV = new RectHV(boundRect.getXmin(), boundRect.getYmin(), boundRect.getXmax(), boundRect.getYmax());
            return new Node(point, rectHV, null, null);
        }

        if (point.compareTo(node.getP()) == 0) {
            node.setP(point);
            return node;
        }

        if (compareByX && point.x() >= node.getP().x()) {
            boundRect.setXmin(node.getP().x());
            node.rt = put(node.rt, point, !compareByX, boundRect);
        } else if (compareByX && point.x() < node.getP().x()) {
            boundRect.setXmax(node.getP().x());
            node.lb = put(node.lb, point, !compareByX, boundRect);
        } else if (!compareByX && point.y() >= node.getP().y()) {
            boundRect.setYmin(node.getP().y());
            node.rt = put(node.rt, point, !compareByX, boundRect);
        } else if (!compareByX && point.y() < node.getP().y()) {
            boundRect.setYmax(node.getP().y());
            node.lb = put(node.lb, point, !compareByX, boundRect);
        }

        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return containsInner(root, p, true);
    }

    private boolean containsInner(Node node, Point2D point, boolean compareByX) {
        if (node == null) return false;
        if (point.compareTo(node.getP()) == 0) return true;

        if (compareByX && point.x() >= node.getP().x()) {
            return containsInner(node.rt, point, !compareByX);
        } else if (compareByX && point.x() < node.getP().x()) {
            return containsInner(node.lb, point, !compareByX);
        } else if (!compareByX && point.y() >= node.getP().y()) {
            return containsInner(node.rt, point, !compareByX);
        } else if (!compareByX && point.y() < node.getP().y()) {
            return containsInner(node.lb, point, !compareByX);
        }

        return false;
    }

    public void draw() {
        drawInner(root, true);
    }

    private void drawInner(Node node, boolean drawVerticalLine) {
        if (node == null) return;

        StdDraw.setPenRadius(0.003);
        if (drawVerticalLine) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.getP().x(), node.getRect().ymin(), node.getP().x(), node.getRect().ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.getRect().xmin(), node.getP().y(), node.getRect().xmax(), node.getP().y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.filledCircle(node.getP().x(), node.getP().y(), 0.01);

        drawInner(node.lb, !drawVerticalLine);
        drawInner(node.rt, !drawVerticalLine);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> result = new ArrayList<>();
        rangeInner(root, rect, result);
        return result;
    }

    private void rangeInner(Node node, RectHV rect, ArrayList<Point2D> result) {
        if (node == null || !rect.intersects(node.getRect())) return;
        if (rect.contains(node.getP())) result.add(node.getP());
        rangeInner(node.lb, rect, result);
        rangeInner(node.rt, rect, result);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        nearest = root.getP();
        nearestInner(root, p);
        return nearest;
    }

    private void nearestInner(Node node, Point2D query) {
        if (node == null || (query.distanceSquaredTo(nearest) < node.getRect().distanceSquaredTo(query)))
            return;

        if (query.distanceSquaredTo(node.getP()) < query.distanceSquaredTo(nearest))
            nearest = node.getP();

        if (node.lb != null && node.lb.getRect().contains(query)) {
            nearestInner(node.lb, query);
            nearestInner(node.rt, query);
        } else if (node.rt != null && node.rt.getRect().contains(query)) {
            nearestInner(node.rt, query);
            nearestInner(node.lb, query);
        }
    }

    private class RectHolder {
        private double xmin;
        private double ymin;
        private double xmax;
        private double ymax;

        public RectHolder(double xmin, double ymin, double xmax, double ymax) {
            this.xmin = xmin;
            this.ymin = ymin;
            this.xmax = xmax;
            this.ymax = ymax;
        }

        public double getXmin() {
            return xmin;
        }

        public void setXmin(double xmin) {
            this.xmin = xmin;
        }

        public double getYmin() {
            return ymin;
        }

        public void setYmin(double ymin) {
            this.ymin = ymin;
        }

        public double getXmax() {
            return xmax;
        }

        public void setXmax(double xmax) {
            this.xmax = xmax;
        }

        public double getYmax() {
            return ymax;
        }

        public void setYmax(double ymax) {
            this.ymax = ymax;
        }
    }

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }

        public Point2D getP() {
            return p;
        }

        public void setP(Point2D p) {
            this.p = p;
        }

        public RectHV getRect() {
            return rect;
        }

        public void setRect(RectHV rect) {
            this.rect = rect;
        }

        public Node getLb() {
            return lb;
        }

        public void setLb(Node lb) {
            this.lb = lb;
        }

        public Node getRt() {
            return rt;
        }

        public void setRt(Node rt) {
            this.rt = rt;
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }
        RectHV query = new RectHV(0.1, 0.1, 0.3, 0.3);
        Stopwatch stopwatch = new Stopwatch();
        ArrayList<Point2D> result = (ArrayList<Point2D>) kdtree.range(query);

        System.out.println("KdTree range size");
        System.out.println(result.size());

        System.out.println("KdTree range time");
        System.out.println(stopwatch.elapsedTime());

        stopwatch = new Stopwatch();
        result = (ArrayList<Point2D>) brute.range(query);

        System.out.println("Brute range size");
        System.out.println(result.size());
        System.out.println("Brute range time");
        System.out.println(stopwatch.elapsedTime());

        stopwatch = new Stopwatch();
        System.out.println("------------");
        System.out.println("KdTree nearest");
        System.out.println(kdtree.nearest(new Point2D(0.3, 0.3)));

        System.out.println("KdTree nearest time");
        System.out.println(stopwatch.elapsedTime());

        stopwatch = new Stopwatch();

        System.out.println("Brute nearest");
        System.out.println(brute.nearest(new Point2D(0.3, 0.3)));

        System.out.println("Brute nearest time");
        System.out.println(stopwatch.elapsedTime());
    }
}
