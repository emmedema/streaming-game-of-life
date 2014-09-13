import org.junit.Before;
import org.junit.Test;
import state.Board;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the <code>Board</code> class.
 *
 * @author Ross Huggett
 */
public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board(10,10);
    }

    @Test
    public void getLiveNeighboursShouldHaveALiveNeighbourAtZeroZero() {
        board.setCell(1, 1, true);

        assertEquals(1, board.getLiveNeighbours(0, 0));
    }

    @Test
    public void getLiveNeighboursShouldHaveNoLiveNeighboursAtOneOne() {
        board.setCell(1, 1, true);

        assertEquals(0, board.getLiveNeighbours(1, 1));
    }

    /**
     * Create a boat @see {@linktourl http://en.wikipedia.org/wiki/Conway's_Game_of_Life#Examples_of_patterns}
     * and make sure it has the correct number of neighbours.
     */
    @Test
    public void createBoatShouldHaveCorrectLivingNeighbours(){
        board.setCell(0, 0, true);
        board.setCell(1, 0, true);
        board.setCell(0, 1, true);
        board.setCell(2, 1, true);
        board.setCell(1, 2, true);

        assertEquals(5, board.getLiveNeighbours(1, 1));
        assertEquals(3, board.getLiveNeighbours(1, 0));
        assertEquals(2, board.getLiveNeighbours(2, 0));
    }

    /**
     * Create a blinker @see {@linktourl http://en.wikipedia.org/wiki/Conway's_Game_of_Life#Examples_of_patterns}
     * and make sure it changes.
     */
    @Test
    public void createBlinkerAndUpdateBoardShouldTransitionCells(){
        board.setCell(0, 1, true);
        board.setCell(1, 1, true);
        board.setCell(2, 1, true);

        board.updateBoard();

        assertEquals(true, board.getCell(1, 0));
        assertEquals(true, board.getCell(1, 1));
        assertEquals(true, board.getCell(1, 2));
    }
}
