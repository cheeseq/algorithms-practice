import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gridSideSize;
    private WeightedQuickUnionUF uf;
    private boolean[][] stateMatrix;
    private int virtualTopIndex;
    private int virtualBottomIndex;
    private int countOfOpenSites = 0;

    public Percolation(int n) {
        if(n <= 0) {
            throw new IllegalArgumentException();
        }

        gridSideSize = n;
        uf = new WeightedQuickUnionUF((n*n)+2);

        initializeStateMatrix();

        virtualTopIndex = (n*n)+1;
        virtualBottomIndex = (n*n);

        createVirtualTopSite();
        createVirtualBottomSite();
    }

    private void initializeStateMatrix() {
        stateMatrix = new boolean[gridSideSize][gridSideSize];
        for(int y = 0; y < gridSideSize; y++) {
            for(int x = 0; x < gridSideSize; x++) {
                stateMatrix[y][x] = false;
            }
        }
    }

    private void createVirtualTopSite() {
        for(int p = getUFIndexFromMatrixCoords(1, 1); p < gridSideSize; p++) {
            uf.union(p, virtualTopIndex);
        }
    }

    private void createVirtualBottomSite() {
        for(int p = getUFIndexFromMatrixCoords(gridSideSize, 1); p <= getUFIndexFromMatrixCoords(gridSideSize, gridSideSize); p++) {
            uf.union(p, virtualBottomIndex);
        }
    }

    public void open(int row, int col) {
        validate(row, col);

        if(isOpen(row, col)) {
            return;
        }

        stateMatrix[row-1][col-1] = true;
        countOfOpenSites++;
        int ufIndex = getUFIndexFromMatrixCoords(row, col);

        if(row > 1 && isOpen(row-1, col)) uf.union(ufIndex, getUFIndexFromMatrixCoords(row-1, col));
        if(row < gridSideSize && isOpen(row+1, col)) uf.union(ufIndex, getUFIndexFromMatrixCoords(row+1, col));
        if(col > 1 && isOpen(row, col-1)) uf.union(ufIndex, getUFIndexFromMatrixCoords(row, col-1));
        if(col < gridSideSize && isOpen(row, col+1)) uf.union(ufIndex, getUFIndexFromMatrixCoords(row, col+1));
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return stateMatrix[row-1][col-1];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) && uf.connected(getUFIndexFromMatrixCoords(row, col), virtualTopIndex);
    }

    public int numberOfOpenSites() {
        return countOfOpenSites;
    }

    public boolean percolates() {
        return uf.connected(virtualBottomIndex, virtualTopIndex);
    }

    private int getUFIndexFromMatrixCoords(int row, int col) {
        if(row <= 1) {
            return col-1;
        }

        return (row-1)*gridSideSize + (col - 1);
    }

    private void validate(int row, int col) {
        if(row < 1 || row > gridSideSize || col < 1 || col > gridSideSize) {
            throw new IllegalArgumentException();
        }
    }
}