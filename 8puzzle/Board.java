import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private int blankTileX = -1;
    private int blankTileY = -1;

    public Board(int[][] tiles) {
        if (tiles == null || tiles.length < 2 || tiles.length > 128) {
            throw new IllegalArgumentException();
        }

        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    this.blankTileX = j;
                    this.blankTileY = i;
                }
            }
        }
    }

    public String toString() {
        int n = dimension();
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public int dimension() {
        return tiles.length;
    }

    public int hamming() {
        int hamming = 0;
        int currentPosition = 1;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != currentPosition) {
                    hamming++;
                }
                currentPosition++;
            }
        }

        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int x = tiles[i][j];
                if (x == 0) {
                    continue;
                }
                int rowIdx;
                int colIdx;
                if (x % dimension() == 0) {
                    rowIdx = (x / dimension() - 1);
                    colIdx = dimension() - 1;
                } else {
                    rowIdx = x / dimension();
                    colIdx = x % dimension() - 1;
                }

                manhattan += Math.abs(rowIdx - i) + Math.abs(colIdx - j);
            }
        }

        return manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        final Board that = (Board) y;
        return Arrays.deepEquals(tiles, that.tiles);
    }

    public Iterable<Board> neighbors() {
        Stack<Board> result = new Stack<>();
        int dimensionIndex = dimension() - 1;
        if (blankTileX < dimensionIndex) {
            result.push(cloneBoardWithSwappedPoints(blankTileX, blankTileY, blankTileX + 1, blankTileY));
        }
        if (blankTileX > 0) {
            result.push(cloneBoardWithSwappedPoints(blankTileX, blankTileY, blankTileX - 1, blankTileY));
        }
        if (blankTileY < dimensionIndex) {
            result.push(cloneBoardWithSwappedPoints(blankTileX, blankTileY, blankTileX, blankTileY + 1));
        }
        if (blankTileY > 0) {
            result.push(cloneBoardWithSwappedPoints(blankTileX, blankTileY, blankTileX, blankTileY - 1));
        }
        return result;
    }

    public Board twin() {
        int x1 = -1;
        int y1 = -1;
        int x2 = -1;
        int y2 = -1;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }

                if (x1 == -1 && y1 == -1) {
                    x1 = j;
                    y1 = i;
                } else if (x2 == -1 && y2 == -1) {
                    x2 = j;
                    y2 = i;
                } else {
                    return cloneBoardWithSwappedPoints(x1, y1, x2, y2);
                }
            }
        }
        return null;
    }

    private Board cloneBoardWithSwappedPoints(int x1, int y1, int x2, int y2) {
        int[][] tilesCopy = copyTiles(tiles);
        exchange(tilesCopy, x1, y1, x2, y2);

        return new Board(tilesCopy);
    }

    private int[][] copyTiles(int[][] input) {
        int[][] copy = new int[input.length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                copy[i][j] = input[i][j];
            }
        }
        return copy;
    }

    private void exchange(int[][] arr, int x1, int y1, int x2, int y2) {
        int p1copy = arr[y1][x1];
        arr[y1][x1] = arr[y2][x2];
        arr[y2][x2] = p1copy;
    }

    public static void main(String[] args) {
        In in = new In("puzzle2x2-00.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        System.out.println("simple to string");
        System.out.println(board.toString());

        System.out.println("dimension");
        System.out.println(board.dimension());

        System.out.println("hamming");
        System.out.println(board.hamming());

        System.out.println("manhattan");
        System.out.println(board.manhattan());

        System.out.println("isGoal");
        System.out.println(board.isGoal());

        System.out.println("twin");
        System.out.println(board.twin().toString());

        System.out.println("neighbors");
        Iterable<Board> neighbors = board.neighbors();
        for (Board b : neighbors) {
            System.out.println(b.toString());
        }

        System.out.println("equals - should be false");
        System.out.println(board.equals(board.twin()));

        System.out.println("equals - should be true");
        System.out.println(board.equals(board));
    }
}
