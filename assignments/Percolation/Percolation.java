import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* *****************************************************************************
 *  Name:   Daniel
 *
 *  Description:   A program to estimate the value of the percolation threshold via Monte Carlo simulation.
 *
 **************************************************************************** */

public class Percolation {
    private int size;
    private int opened;
    private boolean[] siteState;                  // True for opened, false for blocked
    private boolean[] connectTop;                 // True for connected to virtual top site
    private boolean[] connectBottom;              // True for connected to virtual bottom site
    private WeightedQuickUnionUF grid;
    private boolean pFlag;

    // Create an N-by-N grid with all sites blocked
    public Percolation(int n) {
        if(n < 1) {
            throw new IllegalArgumentException("The size of the system should be positive");
        }

        opened = 0;
        size = n;
        int siteCount = n * n;
        pFlag = false;

        grid = new WeightedQuickUnionUF(siteCount);
        siteState = new boolean[siteCount];
        connectTop = new boolean[siteCount];
        connectBottom = new boolean[siteCount];

        // Initialize all sites to be blocked and not connected to the two virtual sites
        for(int i = 0; i < siteCount; ++i) {
            siteState[i] = false;
            connectTop[i] = false;
            connectBottom[i] = false;
        }
    }

    private int xyTo1D(int r, int c) {
        // r and c are of range 1 ~ size
        if(r < 1 || r > size)
            throw new IllegalArgumentException("row index r out of bounds");
        if(c < 1 || c > size)
            throw new IllegalArgumentException("column index c out of bounds");
        return (r - 1) * size + c - 1;
    }

    public void open(int r, int c) {
        int idx = xyTo1D(r, c);
        boolean top = false;
        boolean bottom = false;
        if(isOpen(r, c))
            return;              // Do nothing is the site is already opened
        siteState[idx] = true;
        opened++;

        if(r != 1 && isOpen(r - 1, c)) {
            if(connectTop[grid.find(idx)] || connectTop[grid.find(idx - size)])
                top = true;
            if(connectBottom[grid.find(idx)] || connectBottom[grid.find(idx - size)])
                bottom = true;
            grid.union(idx, idx - size);
        }
        if(r != size && isOpen(r + 1, c)) {
            if(connectTop[grid.find(idx)] || connectTop[grid.find(idx + size)])
                top = true;
            if(connectBottom[grid.find(idx)] || connectBottom[grid.find(idx + size)])
                bottom = true;
            grid.union(idx, idx + size);
        }
        if(c != 1 && isOpen(r, c - 1)) {
            if(connectTop[grid.find(idx)] || connectTop[grid.find(idx - 1)])
                top = true;
            if(connectBottom[grid.find(idx)] || connectBottom[grid.find(idx - 1)])
                bottom = true;
            grid.union(idx, idx - 1);
        }
        if(c != size && isOpen(r, c + 1)) {
            if(connectTop[grid.find(idx)] || connectTop[grid.find(idx + 1)])
                top = true;
            if(connectBottom[grid.find(idx)] || connectBottom[grid.find(idx + 1)])
                bottom = true;
            grid.union(idx, idx + 1);
        }

        // Respectively connect top-line site or bottom-line site to the virtual top or bottom site
        if(r == 1) {
            top = true;
        }
        if(r == size) {
            bottom = true;
        }

        int rootIdx = grid.find(idx);
        connectTop[rootIdx] = top;
        connectBottom[rootIdx] = bottom;
        // Check if the system is percolated
        if(connectTop[rootIdx] && connectBottom[rootIdx]) {
            pFlag = true;
        }
    }

    // Check if the site (r, c) is open
    public boolean isOpen(int r, int c) {
        int idx = xyTo1D(r, c);
        return siteState[idx];
    }

    // Check if the site (r, c) can be connected to the virtual top site
    public boolean isFull(int r, int c) {
        int idx = xyTo1D(r , c);
        return connectTop[grid.find(idx)];
    }

    public boolean percolates() {
        return pFlag;
    }

    public int numberOfOpenSites() {
        return opened;
    }
}
