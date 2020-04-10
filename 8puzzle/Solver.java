import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode goal;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        SearchNode twinGoal;
        SearchNode initialSearchNode = new SearchNode(initial, null, 0);
        SearchNode twinInitialSearchNode = new SearchNode(initial.twin(), null, 0);
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        pq.insert(initialSearchNode);
        twinPQ.insert(twinInitialSearchNode);

        boolean processTwin = false;

        goal = initialSearchNode;
        twinGoal = twinInitialSearchNode;

        while (!goal.getValue().isGoal() && !twinGoal.getValue().isGoal()) {
            if (!processTwin) {
                goal = dequeueAndAddNeighbors(pq);
            } else {
                twinGoal = dequeueAndAddNeighbors(twinPQ);
            }

            processTwin = !processTwin;
        }
    }

    private SearchNode dequeueAndAddNeighbors(MinPQ<SearchNode> pq) {
        SearchNode dequeuedNode = pq.delMin();

        for (Board neighbor : dequeuedNode.getValue().neighbors()) {
            if (dequeuedNode.getPrevious() == null || !neighbor.equals(dequeuedNode.getPrevious().getValue())) {
                pq.insert(new SearchNode(neighbor, dequeuedNode, dequeuedNode.getMoves()+1));
            }
        }

        return dequeuedNode;
    }

    public boolean isSolvable() {
        return goal.getValue().isGoal();
    }

    public int moves() {
        return isSolvable() ? goal.getMoves() : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> solution = new Stack<>();
        SearchNode searchNode = goal;
        while (searchNode != null) {
            solution.push(searchNode.getValue());
            searchNode = searchNode.getPrevious();
        }
        return solution;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board value;
        private final SearchNode previous;
        private final int moves;
        private final int manhattan;

        public SearchNode(Board value, SearchNode previous, int moves) {
            this.value = value;
            this.previous = previous;
            this.moves = moves;
            this.manhattan = value.manhattan();
        }

        public Board getValue() {
            return value;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int getMoves() {
            return moves;
        }

        public int getManhattan() {
            return manhattan;
        }

        public int compareTo(SearchNode that) {
            if ((manhattan+moves) > (that.getManhattan() + that.getMoves())) {
                return 1;
            }
            if ((manhattan+moves) < (that.getManhattan() + that.getMoves())) {
                return -1;
            }
            return 0;
        }
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
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        }
        else {
            for (Board board : solver.solution())
                StdOut.println(board);
        }

        StdOut.println("Minimum number of moves = " + solver.moves());
    }
}
