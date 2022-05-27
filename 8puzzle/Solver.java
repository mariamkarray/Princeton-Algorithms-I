import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {

    private class PQOrder implements Comparator<Node> {
        public int compare(Node o1, Node o2) {
            return Integer.compare(o1.priority, o2.priority);
        }
    }

    private class Node {
        private final Board searchNode;
        private final Node prevNode;
        private int priority;
        private int moves = 0;
        private int manhattan;

        private Node(Board initial, Node prevNode) {
            searchNode = initial;
            this.prevNode = prevNode;
            if (prevNode != null)
                moves = prevNode.moves + 1;
        }
    }

    private Node goal;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Board can't be null");

        PQOrder order = new PQOrder();
        MinPQ<Node> pq = new MinPQ<Node>(order);
        MinPQ<Node> twin = new MinPQ<Node>(order); // is it solvable?
        // casting board into a node
        Node init = new Node(initial, null);
        Node initTwin = new Node(initial.twin(), null);

        pq.insert(init);
        twin.insert(initTwin);

        // roots
        Node min = pq.delMin();
        Node twinMin = twin.delMin();

        if (min.searchNode.isGoal()) {
            goal = min;
            return;
        }
        if (twinMin.searchNode.isGoal()) {
            goal = null;
            return;
        }
        while (true) {
            for (Board neighbor : min.searchNode.neighbors()) {
                Node node = new Node(neighbor, min);
                if (min.prevNode != null) {
                    if (!neighbor.equals(min.prevNode.searchNode)) {
                        node.manhattan = neighbor.manhattan();
                        node.priority = node.moves + node.manhattan;
                        pq.insert(node);
                    }
                }
                else {
                    node.manhattan = neighbor.manhattan();
                    node.priority = node.moves + node.manhattan;
                    pq.insert(node);
                }
            }
            min = pq.delMin();
            if (min.searchNode.isGoal()) {
                goal = min;
                return;
            }
            for (Board neighbor : twinMin.searchNode.neighbors()) {
                Node node = new Node(neighbor, twinMin);
                if (twinMin.prevNode != null) {
                    if (!neighbor.equals(twinMin.prevNode.searchNode)) {
                        node.manhattan = neighbor.manhattan();
                        node.priority = node.moves + node.manhattan;
                        twin.insert(node);
                    }
                }
                else {
                    node.manhattan = neighbor.manhattan();
                    node.priority = node.moves + node.manhattan;
                    twin.insert(node);
                }
            }
            twinMin = twin.delMin();
            if (twinMin.searchNode.isGoal()) {
                goal = null;
                return;
            }
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (goal == null) return false;
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return goal.moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> boardsStack = new Stack<>();
        Node n = goal;
        while (n.prevNode != null) {
            boardsStack.push(n.searchNode);
            n = n.prevNode;
            if (n.prevNode == null)
                boardsStack.push(n.searchNode);
        }
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

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
