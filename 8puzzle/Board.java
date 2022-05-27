/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Board {
    // board array
    private final int[][] board;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        // count number of tiles out of place
        int counter = 0;
        // numbering the tiles from 1 : (N*N - 1), and the last tile will be 0
        int numbering = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // while it's not the last tile, compare the tile value with the "numbering" var
                if (!(i == n - 1 && j == n - 1)) {
                    if (board[i][j] != numbering)
                        counter++;
                }
                numbering++;
            }
        }
        return counter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = board[i][j];
                if (val != 0) {
                    int X2 = (val - 1) / n;
                    int Y2 = (val - 1) % n;
                    sum += Math.abs(i - X2) + Math.abs(j - Y2);
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        // check if y an instance of board or not
        if (!(y.getClass() == this.getClass())) return false;
        // casting
        Board yy = (Board) y;
        // compare dimensions
        if (yy.dimension() != this.dimension()) return false;
        return (yy.toString().equals(this.toString()));
    }

    // The neighbors() method returns an iterable containing the neighbors of the board.
    // Depending on the location of the blank square, a board can have 2, 3, or 4 neighbors.
    public Iterable<Board> neighbors() {
        Stack<Board> boardsStack = new Stack<>();
        int[] pos = findPos();
        if (pos[0] > 0)
            boardsStack.push(swap(pos[0], pos[1], pos[0] - 1, pos[1]));
        if (pos[1] > 0)
            boardsStack.push(swap(pos[0], pos[1], pos[0], pos[1] - 1));
        if (pos[1] < n - 1)
            boardsStack.push(swap(pos[0], pos[1], pos[0], pos[1] + 1));
        if (pos[0] < n - 1)
            boardsStack.push(swap(pos[0], pos[1], pos[0] + 1, pos[1]));
        return () -> new Iterator<Board>() {
            @Override
            public boolean hasNext() {
                if (boardsStack.isEmpty())
                    return false;
                return true;
            }

            @Override
            public Board next() {
                if (!hasNext()) throw new NoSuchElementException("empty stack");
                return boardsStack.pop();
            }
        };
    }

    private int[] findPos() {
        int[] pos = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

    private Board swap(int blankX, int blankY, int toX, int toY) {
        int[][] neighborArr = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                neighborArr[i][j] = board[i][j];
            }
        }
        int temp = neighborArr[toX][toY];
        neighborArr[toX][toY] = neighborArr[blankX][blankY];
        neighborArr[blankX][blankY] = temp;
        Board neigbor = new Board(neighborArr);
        return neigbor;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinArr = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinArr[i][j] = board[i][j];
            }
        }
        if (twinArr[0][0] != 0 && twinArr[0][1] != 0) {
            int temp = twinArr[0][0];
            twinArr[0][0] = twinArr[0][1];
            twinArr[0][1] = temp;
        }
        else {
            int temp = twinArr[1][0];
            twinArr[1][0] = twinArr[1][1];
            twinArr[1][1] = temp;
        }
        Board twin = new Board(twinArr);
        return twin;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        for (Board value : initial.neighbors()) {
            StdOut.println(value.manhattan());
            StdOut.println(value);
        }
    }
}
