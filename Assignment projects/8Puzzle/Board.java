import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int dim;
    private final int[][] blocks;

    public Board(int[][] blocks) {
        this.blocks = blockCopy(blocks);
        dim = blocks.length;
    }

    public int dimension() {
        return dim;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (blocks[i][j] != i * dim + j + 1)
                    count++;
            }
        }
        return count - 1;
    }

    public int manhattan() {
        int count = 0;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (blocks[i][j] > 0) {
                    int value = blocks[i][j] - 1;
                    int row = value / dim;
                    int col = value % dim;
                    count += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return count;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        // a board that is obtained by exchanging a pair of adjacent blocks in the same row

        int[][] copy = blockCopy(blocks);
        if (dim <= 1)
            return new Board(copy);

        for (int i = 0; i < dim; ++i) {
            int lastValue = copy[i][0];

            for (int j = 1; j < dim; ++j) {
                int value = copy[i][j];
                if (value != 0 && lastValue != 0) {
                    copy[i][j] = lastValue;
                    copy[i][j - 1] = value;
                    return new Board(copy);
                }
                lastValue = value;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<>();

        int[] empty = findEmpty();
        int emptyRow = empty[0];
        int emptyCol = empty[1];

        if (emptyRow > 0) {
            int[][] copy = blockCopy(blocks);
            swap(copy, emptyRow, emptyCol, emptyRow - 1, emptyCol);
            queue.enqueue(new Board(copy));
        }

        if (emptyRow < dim - 1) {
            int[][] copy = blockCopy(blocks);
            swap(copy, emptyRow, emptyCol, emptyRow + 1, emptyCol);
            queue.enqueue(new Board(copy));
        }

        if (emptyCol > 0) {
            int[][] copy = blockCopy(blocks);
            swap(copy, emptyRow, emptyCol, emptyRow, emptyCol - 1);
            queue.enqueue(new Board(copy));
        }

        if (emptyCol < dim - 1) {
            int[][] copy = blockCopy(blocks);
            swap(copy, emptyRow, emptyCol, emptyRow, emptyCol + 1);
            queue.enqueue(new Board(copy));
        }

        return queue;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(dim + "\n");
        output.append(toString2D(blocks));
        return output.toString();
    }

    private static int[][] blockCopy(int[][] blocks) {
        int[][] copy = new int[blocks.length][blocks[0].length];
        for (int i = 0; i < blocks.length; ++i) {
            copy[i] = Arrays.copyOf(blocks[i], blocks[i].length);
        }
        return copy;
    }

    private int[] findEmpty() {
        int[] empty = new int[2];
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (blocks[i][j] == 0) {
                    empty[0] = i;
                    empty[1] = j;
                    return empty;
                }
            }
        }
        return empty;
    }

    private void swap(int[][] tempBlocks, int r1, int c1, int r2, int c2) {
        int temp = tempBlocks[r1][c1];
        tempBlocks[r1][c1] = tempBlocks[r2][c2];
        tempBlocks[r2][c2] = temp;
    }

    private String toString2D(int[][] input) {
        StringBuilder patternStr = new StringBuilder();
        int length = input.length;
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                patternStr.append(String.format("%2d ", input[i][j]));
            }
            if (i < length - 1)
                patternStr.append("\n");
        }
        return patternStr.toString();
    }
}
