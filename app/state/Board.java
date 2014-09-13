package state;

/**
 * A board to play Conway's Game Of Life. @see {@linktourl http://en.wikipedia.org/wiki/Conway's_Game_of_Life}
 * for the details.
 *
 * @author Ross Huggett
 */
public class Board {

    private int width;
    private int height;

    private boolean[][] board;

    // Used to calculate the position of the cells neighbours
    private static int[][] neighbourOffsets = {
            {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1},  {-1, 1}, {-1, 0}, {-1, -1}
    };

    /**
     * Constructor for the board.
     *
     * @param width the width of the board
     * @param height the height of the board
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;

        board = new boolean[width][height];

        // Initialise the board to false.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[x][y] = false;
            }
        }
    }

    /**
     * Gets the live neighbours given cell coordinates on the board.
     *
     * @param x x coordinate on the board
     * @param y y coordinate on the board
     * @return the number of live neighbours
     */
    public int getLiveNeighbours(int x, int y) {
        int liveNeighbours = 0;

        for (int i = 0; i < 8; i++) {
            int neighbourX = x + neighbourOffsets[i][0];
            int neighbourY = y + neighbourOffsets[i][1];

            // This has to be done in case we hit a point on the board which doesn't have eight neighbours i.e. cells
            // in the corner.
            try {
                if (board[neighbourX][neighbourY])
                    liveNeighbours++;
            } catch (ArrayIndexOutOfBoundsException e) {
                // Do nothing.
            }
        }
        return liveNeighbours;
    }

    /**
     * Updates the board with the new values.
     */
    public void updateBoard(){
        boolean[][] newBoard = new boolean[width][height];

        // Iterate round the board, getting the live neighbours for each cell and assigning them to the new board.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int liveNeighbours = getLiveNeighbours(x, y);
                newBoard[x][y] = transitionCell(getCell(x, y), liveNeighbours);
            }
        }
        board = newBoard;
    }

    /**
     * Transitions the cell based on if it is alive and how many neighbours it has.
     * @see {@linktourl http://en.wikipedia.org/wiki/Conway's_Game_of_Life#Rules}
     *
     * @param isAlive if the cell is alive or not
     * @param liveNeighbours how many live neighbours it has
     * @return the cells new transition
     */
    private boolean transitionCell(boolean isAlive, int liveNeighbours) {
        if (isAlive) {
            return (liveNeighbours == 2 || liveNeighbours == 3) ;
        } else {
            return (liveNeighbours == 3);
        }
    }

    public boolean getCell(int x, int y) {
        return board[x][y];
    }

    public void setCell(int x, int y, boolean state) {
        board[x][y] = state;
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(board[x][y]) {
                    boardString.append("[X]");
                } else {
                    boardString.append("[O]");
                }
            }
            boardString.append("\n");
        }

        return boardString.toString();
    }
}
