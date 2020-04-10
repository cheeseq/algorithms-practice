import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        if (points.length == 1) {
            return;
        }

        Arrays.sort(points);

        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            copy[i] = points[i];
        }

        for (Point p : points) {
            Arrays.sort(copy, p.slopeOrder());
            double previousSlope = Double.NEGATIVE_INFINITY;
            ArrayList<Point> collinearPoints = new ArrayList<>();
            collinearPoints.add(p);

            for (int i = 1; i < copy.length; i++) {
                if (copy[i].slopeTo(p) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException();
                }

                if (copy[i].slopeTo(p) != previousSlope) {
                    if (collinearPoints.size() >= 4) {
                        Point[] slopes = new Point[collinearPoints.size()];
                        collinearPoints.toArray(slopes);
                        Arrays.sort(slopes);
                        if (p.compareTo(slopes[0]) == 0) {
                            segments.add(new LineSegment(p, slopes[slopes.length-1]));
                        }
                    }
                    collinearPoints.clear();
                    collinearPoints.add(p);
                    previousSlope = copy[i].slopeTo(p);
                    collinearPoints.add(copy[i]);
                } else {
                    collinearPoints.add(copy[i]);
                }
            }

            if (collinearPoints.size() >= 4) {
                Point[] slopes = new Point[collinearPoints.size()];
                collinearPoints.toArray(slopes);
                Arrays.sort(slopes);
                if (p.compareTo(slopes[0]) == 0) {
                    segments.add(new LineSegment(p, slopes[slopes.length - 1]));
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[numberOfSegments()];
        return segments.toArray(res);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            if (segment != null) {
                StdOut.println(segment);
                segment.draw();
                StdDraw.show();
            }
        }
        StdDraw.show();
    }
}
