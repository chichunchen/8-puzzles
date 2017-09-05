import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by chichunchen on 9/4/17.
 */
public class Board {

    final private int[][] blocks;
    final private int dimension;
    private int blank;
    final private int hamming;
    final private int manhattan;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
        this.blocks = blocks;
        this.dimension = blocks.length;
        int total = dimension * dimension;
        int ham = 0, man = 0;

        int index = 1;

        // compute hamming and find blank position
        for (int[] arr : blocks) {
            for (int curr_elem: arr) {

                // compute hamming
                if (index != curr_elem && index < total) {
                    ham++;
                }

                // compute manhattan
                int dest = curr_elem,
                    curr = index;
                // don't compute 0
                if (curr_elem != 0) {
                    int curr_y = (int) Math.ceil((double)curr / this.dimension);
                    int dest_y = (int) Math.ceil((double)dest / this.dimension);
                    int curr_x = curr % this.dimension == 0 ? this.dimension : curr % this.dimension;
                    int dest_x = dest % this.dimension == 0 ? this.dimension : dest % this.dimension;

//                    StdOut.println("curr " + curr + " dest " + dest);
//                    StdOut.println("curr_x " + curr_x + " curr_y " + curr_y);
//                    StdOut.println("dest_x " + dest_x + " dest_y " + dest_y);

                    // y
                    while (curr_y != dest_y) {
                        // go up
                        if(curr_y > dest_y) {
//                            StdOut.println("curr " + dest + " goes up");
                            curr_y--;
                            man++;
                        }
                        // go down
                        else if(curr_y < dest_y) {
//                            StdOut.println("curr " + dest + " goes down");
                            curr_y++;
                            man++;
                        }
                    }
                    // x
                    while (curr_x != dest_x) {
                        // go left
                        if(curr_x > dest_x) {
//                            StdOut.println("curr " + dest + " goes left");
                            curr_x--;
                            man++;
                        }
                        // go right
                        else if(curr_x < dest_x) {
//                            StdOut.println("curr " + dest + " goes right");
                            curr_x++;
                            man++;
                        }
                    }
                }

                // find blank
                if (curr_elem == 0) {
                    blank = index;
                }
                index++;
            }
        }

        this.hamming = ham;
        this.manhattan = man;
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    {
        return dimension;
    }

    public int hamming()                   // number of blocks out of place
    {
        return hamming;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        return manhattan;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        if (this.hamming == 0)
            return true;
        else
            return false;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int[][] t_blocks = new int[this.dimension][this.dimension];

        // copy all block elements
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                t_blocks[i][j] = this.blocks[i][j];
            }
        }

        // swap elements that is neighbor also both not 0
        for (int j = 0; j < this.dimension; j++) {
            if (t_blocks[0][j] != 0) {
                int dest_x = j, dest_y = 0;

                if ((dest_x+1) < dimension && (t_blocks[0][j+1] != 0)) {
                    dest_x++;
                }
                else if ((dest_x-1) > 0 && (t_blocks[0][j-1] != 0)) {
                    dest_x--;
                }
                else if ((dest_y+1) < dimension && (t_blocks[1][j] != 0)) {
                    dest_y++;
                }
//                StdOut.println("dest_x " + dest_x + " dest_y " + dest_y);

                int temp;
                temp = t_blocks[0][j];
                t_blocks[0][j] = t_blocks[dest_y][dest_x];
                t_blocks[dest_y][dest_x] = temp;

                break;
            }
        }
        return new Board(t_blocks);
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == null) {
            return false;
        }
        else {
            return this.toString().equals(y.toString());
        }
    }

    /* deep copy 2-dimension block */
    private int[][] copyBlocks(int [][]tiles) {
        int[][] result = new int[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                result[i][j] = tiles[i][j];
            }
        }
        return result;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> stack = new Stack<>();
        int blank_x = (this.blank % this.dimension == 0 ? this.dimension : this.blank % this.dimension)-1;
        int blank_y = (int) Math.ceil((double) this.blank / this.dimension)-1;

//        StdOut.println("blank_x: " + blank_x + " blank_y: " + blank_y);

        // add Board that swap with right
        if ((blank_x+1) < this.dimension) {
//            StdOut.println("swap with right");

            int[][] tiles = copyBlocks(this.blocks);
            int temp = tiles[blank_y][blank_x];
            tiles[blank_y][blank_x] = tiles[blank_y][blank_x+1];
            tiles[blank_y][blank_x+1] = temp;

            stack.push(new Board(tiles));
        }

        // add Board that swap with left
        if ((blank_x-1) >= 0) {
//            StdOut.println("swap with left");

            int[][] tiles = copyBlocks(this.blocks);
            int temp = tiles[blank_y][blank_x];
            tiles[blank_y][blank_x] = tiles[blank_y][blank_x-1];
            tiles[blank_y][blank_x-1] = temp;

            stack.push(new Board(tiles));
        }

        // add Board that swap with below
        if ((blank_y+1) < this.dimension) {
//            StdOut.println("swap with below");

            int[][] tiles = copyBlocks(this.blocks);
            int temp = tiles[blank_y][blank_x];
            tiles[blank_y][blank_x] = tiles[blank_y+1][blank_x];
            tiles[blank_y+1][blank_x] = temp;

            stack.push(new Board(tiles));
        }

        // add Board that swap with up
        if ((blank_y-1) >= 0) {
//            StdOut.println("swap with up");

            int[][] tiles = copyBlocks(this.blocks);
            int temp = tiles[blank_y][blank_x];
            tiles[blank_y][blank_x] = tiles[blank_y-1][blank_x];
            tiles[blank_y-1][blank_x] = temp;

            stack.push(new Board(tiles));
        }

        return stack;
    }

//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        s.append("Dimension: " + dimension + "\n");
//        s.append("Manhattan: " + manhattan() + "\n");
//        for (int i = 0; i < dimension; i++) {
//            for (int j = 0; j < dimension; j++) {
//                s.append(String.format("%2d ", blocks[i][j]));
//            }
//            s.append("\n");
//        }
//        return s.toString();
//    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder sb = new StringBuilder(this.dimension + "\n");
//        sb.append("Manhattan: " + manhattan() + "\n");
//        sb.append("Hamming: " + hamming() + "\n");
        for (int[] arr : this.blocks) {
            for (int i: arr) {
                sb.append(String.format("%2d ", i));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // test
//            test_neighbor(tiles);
//            test_twin(tiles);
        }
    }

//    static private void test_neighbor(int[][] tiles) {
//        StdOut.println("test_neighbor");
//        Board b1 = new Board(tiles);
//        StdOut.println("b1: \n" + b1 + "\n");
//
//        for (Board b: b1.neighbors()) {
//            StdOut.println(b);
//        }
//    }
//
//    static private void test_twin(int[][] tiles) {
//        StdOut.println("test_twin");
//        Board b1 = new Board(tiles);
//        Board b2 = b1.twin();
//        StdOut.println(b1);
//        StdOut.println(b2);
//
//        assert !b1.equals(b2);
//    }
}