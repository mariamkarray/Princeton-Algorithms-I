import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid;
    private int[][] model; // corresponding int grid
    private int openSitesNum = 0;
    private int val; // val will take the value of n
    private WeightedQuickUnionUF qu;
    private int top, bot; // virtual top and bottom sites

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("out of bounds");
        qu = new WeightedQuickUnionUF(n * n + 2);
        val = n;
        grid = new boolean[(n * n) + 1];
        model = new int[n + 1][n + 1];
        // grid to check if a site is open or not
        for (int i = 1; i <= n * n; i++)
            grid[i] = false;
        int cnt = 1;
        // corresponding int model
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                model[i][j] = cnt;
                cnt++;
            }
        }
    }

    void connect(int row, int col, int neighbourRow, int neighbourCol) {
        if (neighbourRow < 1 || neighbourRow > val || neighbourCol < 1 || neighbourCol > val)
            return;
        if (isOpen(neighbourRow, neighbourCol)) {
            qu.union(model[row][col], model[neighbourRow][neighbourCol]);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > val || col <= 0 || col > val)
            throw new IllegalArgumentException("out of bounds");

        if (isOpen(row, col))
            return;
        grid[model[row][col]] = true; // open the site
        openSitesNum++;

        // virtual sites
        top = 0;
        bot = (val * val) + 1;

        // connect top sites to virtual sites
        if (row == 1) qu.union(top, model[row][col]);
        if (row == val) qu.union(bot, model[row][col]);
        // up
        connect(row, col, row - 1, col);
        // down
        connect(row, col, row + 1, col);
        // left
        connect(row, col, row, col - 1);
        // right
        connect(row, col, row, col + 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > val || col <= 0 || col > val)
            throw new IllegalArgumentException("out of bounds");
        if (grid[model[row][col]])
            return true;
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > val || col <= 0 || col > val)
            throw new IllegalArgumentException("out of bounds");
        if (grid[model[row][col]]) {
            if (qu.find(top) == qu.find(model[row][col]))
                return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesNum;
    }

    // does the system percolate?
    public boolean percolates() {
        if (qu.find(top) == qu.find(bot) && qu.find(top) != 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        
        }
    }
}
