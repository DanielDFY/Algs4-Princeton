import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private static final int OFFSET = 65;

    private Node root;
    private BoggleBoard board;
    private boolean[][] marked;
    private int col, row;

    private Bag<String> result;
    private Bag<Node> nodeToReset;

    public BoggleSolver(String[] dictionaries) {
        root = new Node();
        for (String dictionary : dictionaries) {
            put(dictionary);
        }
    }

    private static class Node {
        private boolean isString = false;
        private Node[] next = new Node[26];
    }

    private Node get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - OFFSET;
        return get(x.next[c], key, d + 1);
    }

    private void put(String key) {
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isString = true;
            return x;
        }
        int c = key.charAt(d) - OFFSET;
        x.next[c] = put(x.next[c], key, d + 1);
        return x;
    }

    public Iterable<String> getAllValidWords(BoggleBoard b) {
        this.board = b;
        this.row = b.rows();
        this.col = b.cols();

        marked = new boolean[row][col];
        result = new Bag<>();
        nodeToReset = new Bag<>();

        Node node;
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                node = root.next[b.getLetter(i, j) - OFFSET];
                dfs(node, i, j, "");
            }
        }
        for (Node n : nodeToReset) n.isString = true;

        return result;
    }

    private void dfs(Node node, int r, int c, String prefix) {
        if (node == null || marked[r][c]) return; // check if next node in tries is null

        char curChar = board.getLetter(r, c);

        if (curChar == 'Q') {
            node = node.next['U' - OFFSET];
        }
        if (node == null) return; // check since node of 'Q' is moved onto node 'U'

        if (curChar == 'Q')  {
            prefix += "QU";
        } else {
            prefix += curChar;
        }


        marked[r][c] = true;

        if (node.isString) {
            nodeToReset.add(node);
            node.isString = false;
            if (prefix.length() > 2) result.add(prefix);
        }

        if (r - 1 >= 0 && c - 1 >= 0)
            dfs(node.next[board.getLetter(r-1, c-1) - OFFSET], r - 1, c - 1, prefix);
        if (r - 1 >= 0)
            dfs(node.next[board.getLetter(r-1, c) - OFFSET], r - 1, c, prefix);
        if (r - 1 >= 0 && c + 1 < col)
            dfs(node.next[board.getLetter(r-1, c+1) - OFFSET], r - 1, c + 1, prefix);
        if (c - 1 >= 0)
            dfs(node.next[board.getLetter(r, c-1) - OFFSET], r, c - 1, prefix);
        if (c + 1 < col)
            dfs(node.next[board.getLetter(r, c+1) - OFFSET], r, c + 1, prefix);
        if (r + 1 < row && c - 1 >= 0)
            dfs(node.next[board.getLetter(r+1, c-1) - OFFSET], r + 1, c - 1, prefix);
        if (r + 1 < row)
            dfs(node.next[board.getLetter(r+1, c) - OFFSET], r + 1, c, prefix);
        if (r + 1 < row && c + 1 < col)
            dfs(node.next[board.getLetter(r+1, c+1) - OFFSET], r + 1, c + 1, prefix);

        marked[r][c] = false;
    }

    public int scoreOf(String word) {
        Node node = get(word);
        if (node == null || !node.isString) return 0;
        int len = word.length();
        if (len < 3) return 0;
        else if (len < 4) return 1;
        else if (len < 7) return len - 3;
        else if (len < 8) return 5;
        else return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word + " " + solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}