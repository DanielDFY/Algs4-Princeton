import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Stack<Board> solutionBoards;
    private boolean isSolvable;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode preNode;
        private final int moves;
        private final int manhattan;
        private final boolean isTwin;

        public SearchNode(Board initial, boolean isTwin, SearchNode preNode) {
            this.board = initial;
            this.preNode = preNode;
            if (preNode == null)
                this.moves = 0;
            else
                this.moves = preNode.moves + 1;
            this.manhattan = initial.manhattan();
            this.isTwin = isTwin;
        }

        public int compareTo(SearchNode that) {
            int priority = this.manhattan + this.moves - that.manhattan - that.moves;
                return priority == 0 ? this.manhattan - that.manhattan : priority;
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Please input a valid board!");

        isSolvable = false;
        solutionBoards = new Stack<>();
        MinPQ<SearchNode> solutionQueue = new MinPQ<>();

        solutionQueue.insert(new SearchNode(initial, false, null));
        solutionQueue.insert(new SearchNode(initial.twin(), true, null));


        while (!solutionQueue.min().board.isGoal()) {
            SearchNode solutionNode = solutionQueue.delMin();
            addSearchNode(solutionQueue, solutionNode);
        }

        SearchNode current = solutionQueue.min();
        while (current.preNode != null) {
            solutionBoards.push(current.board);
            current = current.preNode;
        }
        solutionBoards.push(current.board);

        if (!solutionQueue.min().isTwin)
            isSolvable = true;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        if (!isSolvable())
            return -1;
        return solutionBoards.size() - 1;
    }

    public Iterable<Board> solution() {
        return isSolvable()? solutionBoards : null;
    }

    private void addSearchNode(MinPQ<SearchNode> pq, SearchNode node) {
        for (Board board : node.board.neighbors()) {
            if (node.preNode == null || !board.equals(node.preNode.board)) {
                pq.insert(new SearchNode(board, node.isTwin, node));
            }
        }
    }
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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
