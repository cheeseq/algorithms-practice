import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] attempts;

    public PercolationStats(int n, int trials) {
        if(n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        attempts = new double[trials];

        for(int trial = 0; trial < trials; trial++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                percolation.open(row, col);
            }

            attempts[trial] = (double)percolation.numberOfOpenSites() / (double)(n*n);
        }
    }

    public double mean() {
        return StdStats.mean(attempts);
    }

    public double stddev() {
        return StdStats.stddev(attempts);
    }

    public double confidenceLo() {
        return mean() - ((1.96*stddev())/Math.sqrt(attempts.length));
    }

    public double confidenceHi() {
        return mean() + ((1.96*stddev())/Math.sqrt(attempts.length));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
