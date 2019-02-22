/* *****************************************************************************
 *  Name:   Daniel
 *
 *  Description:   A program to estimate the value of the percolation threshold via Monte Carlo simulation.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] threshold;
    private double mean;
    private double stddev;
    private double bias;

    public PercolationStats(int n, int trials){
        int row;
        int col;

        // Check if n and trails are both positive
        if(n <= 0 || trials <= 0)
            throw new IllegalArgumentException("The size of the system and the number of trails should both be positive.");

        threshold = new double[trials];

        // perform trials independent experiments on an n-by-n grid
        for(int i = 0; i < trials; ++i) {
            Percolation pl = new Percolation(n);
            while(!pl.percolates()) {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
                if(!pl.isOpen(row, col)) {
                    pl.open(row, col);
                }
            }
            threshold[i] = (double)pl.numberOfOpenSites()/(n * n);
        }

        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
        bias = 1.96 * stddev() / Math.sqrt(threshold.length);

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    //sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // Low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - bias;
    }

    // High endpoint of 95% confidence interval
    public  double confidenceHi() {
        return mean + bias;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats pls = new PercolationStats(n, trails);
        System.out.printf("mean                     = %f\n", pls.mean());
        System.out.printf("stddev                   = %f\n", pls.stddev());
        System.out.printf("95%% confidence Interval  = %f, %f\n", pls.confidenceLo(), pls.confidenceHi());
    }
}
