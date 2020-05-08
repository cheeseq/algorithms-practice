import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private Color[][] colors;
    private boolean isTransposed = false;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        energy = new double[picture.width()][picture.height()];
        colors = new Color[picture.width()][picture.height()];
        calcColors();
        calcEnergy();
    }

    private void calcColors() {
        for (int col = 0; col < picture.width(); col++) {
            for (int row = 0; row < picture.height(); row++) {
                colors[col][row] = picture.get(col, row);
            }
        }
    }

    private void calcEnergy() {
        energy = new double[width()][height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                energy[col][row] = energy(col, row);
            }
        }
        isTransposed = false;
    }

    public Picture picture() {
        Picture newPicture = new Picture(width(), height());
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                newPicture.set(col, row, colors[col][row]);
            }
        }
        picture = newPicture;
        return picture;
    }

    public int width() {
        return colors.length;
    }

    public int height() {
        return colors[0].length;
    }

    public int[] findVerticalSeam() {
        if (isTransposed) {
            transpose();
        }
        return findVerticalSeamInner();
    }

    public int[] findHorizontalSeam() {
        if (!isTransposed) {
            transpose();
        }
        return findVerticalSeamInner();
    }

    private void validateSeam(int[] seam, boolean horizDirection) {
        int prevEntry = -1;
        for (int entry : seam) {
            if (entry < 0)
                throw new IllegalArgumentException("Seam entry is outside its prescribed range");
            if (horizDirection && entry >= height())
                throw new IllegalArgumentException("Seam entry is outside its prescribed range");
            else if (!horizDirection && entry >= width())
                throw new IllegalArgumentException("Seam entry is outside its prescribed range");
            if (Math.abs(entry - prevEntry) > 1 && prevEntry != -1)
                throw new IllegalArgumentException("Two adjacent seam entries differ by more than 1");
            prevEntry = entry;
        }
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height() || width() <= 1) {
            throw new IllegalArgumentException();
        }

        validateSeam(seam, false);
        Color[][] newColors = new Color[width() - 1][height()];

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (seam[row] == col) continue;
                int x = col;
                if (x > seam[row])
                    x--;
                newColors[x][row] = colors[col][row];
            }
        }

        colors = newColors;
        calcEnergy();
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width() || height() <= 1) {
            throw new IllegalArgumentException();
        }

        validateSeam(seam, true);
        Color[][] newColors = new Color[width()][height() - 1];

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (seam[col] == row) continue;
                int y = row;
                if (y > seam[col])
                    y--;
                newColors[col][y] = colors[col][row];
            }
        }

        colors = newColors;
        calcEnergy();
    }

    public double energy(int x, int y) {
        if (x >= width() || x < 0 || y >= height() || y < 0) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }

        Color leftX = colors[x-1][y];
        Color rightX = colors[x+1][y];
        Color topY = colors[x][y-1];
        Color bottomY = colors[x][y+1];

        int diffX = square(Math.abs(rightX.getRed() - leftX.getRed())) +
                    square(Math.abs(rightX.getGreen() - leftX.getGreen())) +
                    square(Math.abs(rightX.getBlue() - leftX.getBlue()));

        int diffY = square(Math.abs(bottomY.getRed() - topY.getRed())) +
                    square(Math.abs(bottomY.getGreen() - topY.getGreen())) +
                    square(Math.abs(bottomY.getBlue() - topY.getBlue()));

        return Math.sqrt(diffX + diffY);
    }

    private int square(int number) {
        return number * number;
    }

    private int getWidthTranspositionAware() {
        return !isTransposed ? colors.length : colors[0].length;
    }

    private int getHeightTranspositionAware() {
        return !isTransposed ? colors[0].length : colors.length;
    }

    private void transpose() {
        double[][] transposedEnergy = new double[getHeightTranspositionAware()][getWidthTranspositionAware()];
        for (int col = 0; col < getWidthTranspositionAware(); col++) {
            for (int row = 0; row < getHeightTranspositionAware(); row++) {
                transposedEnergy[row][col] = energy[col][row];
            }
        }
        energy = transposedEnergy;
        isTransposed = !isTransposed;
    }

    private int[] findVerticalSeamInner() {
        double totalEnergy = Double.POSITIVE_INFINITY;
        double[][] distTo = new double[getWidthTranspositionAware()][getHeightTranspositionAware()];
        PixelPoint[][] edgeTo = new PixelPoint[getWidthTranspositionAware()][getHeightTranspositionAware()];
        boolean[][] visited = new boolean[getWidthTranspositionAware()][getHeightTranspositionAware()];

        Queue<PixelPoint> queue = new Queue<>();

        for (int col = 0; col < getWidthTranspositionAware(); col++) {
            for (int row = 0; row < getHeightTranspositionAware(); row++) {
                if (row > 0) {
                    distTo[col][row] = Double.POSITIVE_INFINITY;
                } else {
                    PixelPoint p = new PixelPoint(col, row);
                    distTo[col][row] = getEnergy(p);
                    queue.enqueue(p);
                }
            }
        }

        PixelPoint endpoint = new PixelPoint(-1, getHeightTranspositionAware() - 1);
        while (!queue.isEmpty()) {
            PixelPoint point = queue.dequeue();
            if (visited[point.getX()][point.getY()]) continue;
            if (point.getY() == endpoint.getY() && distTo[point.getX()][point.getY()] < totalEnergy) {
                totalEnergy = distTo[point.getX()][point.getY()];
                endpoint.setX(point.getX());
            }
            for (PixelPoint adj : getAdjacents(point)) {
                if (distTo[point.getX()][point.getY()] + getEnergy(adj) < distTo[adj.getX()][adj.getY()]) {
                    distTo[adj.getX()][adj.getY()] = distTo[point.getX()][point.getY()] + getEnergy(adj);
                    edgeTo[adj.getX()][adj.getY()] = point;
                    queue.enqueue(adj);
                }
            }
            visited[point.getX()][point.getY()] = true;
        }
        return buildFormattedShortestPath(endpoint, edgeTo);
    }

    private int[] buildFormattedShortestPath(PixelPoint from, PixelPoint[][] path) {
        int[] shortestPath = new int[getHeightTranspositionAware()];
        int y = from.getY();
        int x = from.getX();
        shortestPath[y] = x;
        while (path[x][y] != null) {
            PixelPoint edge = path[x][y];
            shortestPath[edge.getY()] = edge.getX();
            x = edge.getX();
            y = edge.getY();
        }
        return shortestPath;
    }

    private double getEnergy(PixelPoint pixelPoint) {
        return energy[pixelPoint.getX()][pixelPoint.getY()];
    }

    private List<PixelPoint> getAdjacents(PixelPoint pixelPoint) {
        List<PixelPoint> result = new ArrayList<>();
        if (pixelPoint.getY() + 1 < getHeightTranspositionAware()) {
            result.add(new PixelPoint(pixelPoint.getX(), pixelPoint.getY() + 1));
            if (pixelPoint.getX() - 1 > 0)
                result.add(new PixelPoint(pixelPoint.getX() - 1, pixelPoint.getY() + 1));
            if (pixelPoint.getX() + 1 < getWidthTranspositionAware())
                result.add(new PixelPoint(pixelPoint.getX() + 1, pixelPoint.getY() + 1));
        }
        return result;
    }

    private class PixelPoint {
        private int x;
        private int y;

        public PixelPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}