import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<>();
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        int n = points.length;
        for (int i = 0; i < n; i++) {
            Point p = points[i];
            if (p == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < n; j++) {
                Point q = points[j];
                if (q == null) throw new IllegalArgumentException();
                for (int k = j + 1; k < n; k++) {
                    Point r = points[k];
                    if (r == null) throw new IllegalArgumentException();
                    for (int m = k + 1; m < n; m++) {
                        Point s = points[m];
                        if (s == null) throw new IllegalArgumentException();

                        double ij = p.slopeTo(q);
                        double ik = p.slopeTo(r);
                        double im = p.slopeTo(s);

                        if (ij == Double.NEGATIVE_INFINITY || ik == Double.NEGATIVE_INFINITY || im == Double.NEGATIVE_INFINITY) {
                            throw new IllegalArgumentException();
                        }

                        if (ij == ik && ij == im) {
                            Point[] pts = new Point[4];
                            pts[0] = p;
                            pts[1] = q;
                            pts[2] = r;
                            pts[3] = s;
                            Arrays.sort(pts);

                            segments.add(new LineSegment(pts[0], pts[3]));
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            if (segment != null) {
                StdOut.println(segment);
                segment.draw();
                StdDraw.show();
            }
        }
        System.out.println(collinear.numberOfSegments());
        StdDraw.show();
    }
}
