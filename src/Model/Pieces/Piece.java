package Model.Pieces;
import Model.PieceEnum;
import Model.Position;
import Model.ChessModel;
import java.util.ArrayList;

/**
 * @author Adam Fowles
 *
 */
public abstract class Piece
{
    public int color;
    protected Position currentPos;
    protected ChessModel model;
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    protected PieceEnum pieceType;
    public boolean isEmpty;
    public boolean hasMoved;

    public Piece(int row, int col, int color, ChessModel model)
    {
        this.currentPos = new Position(row, col);
        this.color = color;
        this.model = model;
        isEmpty = false;
        hasMoved = false;
    }

    /**
     * Default constructor for an "empty" piece.
     * @param row - current row
     * @param col - current column
     */
    public Piece(int row, int col)
    {
        currentPos = new Position(row, col);
        isEmpty = true;
    }

    /**
     * Gets the valid moves for a piece that can move multiple positions.
     * See child class for list of offsets
     * @param rowOffset - up or down the row
     * @param colOffset - up or down the column
     * @param currentCol - current column position of the piece
     * @param currentRow - current row position of the piece
     * @return a list of positions in a single direction that can be moved to.
     */
    protected ArrayList<Position> getValidMultiple(int rowOffset, int colOffset,
                                                        int currentRow, int currentCol)
    {
        ArrayList<Position> validMoves = new ArrayList<Position>();
        int row = currentRow + rowOffset;
        int col = currentCol + colOffset;
        // Check every square in a direction that is on the game board
        while (model.getPieceAt(row,col) != null)
        {
            // If there is nothing there it is a valid spot to move
            if (model.getPieceAt(row, col).isEmpty)
            {
                validMoves.add(new Position(row, col));
            }
            // If there a piece blocking but that piece is of the opposite color it is valid
            else if (model.getPieceAt(row, col).color != this.color)
            {
                validMoves.add(new Position(row,col));
                // cannot continue past the piece however
                break;
            }
            else
            {
                break;
            }
            row += rowOffset;
            col += colOffset;
        }
        return validMoves;
    }

    // All pieces must implement get valid moves
    public abstract ArrayList<Position> getValidMoves();
    public Position getCurrentPos(){return currentPos;}
    public void setCurrentPos(int row, int col){currentPos = new Position(row, col);}
    public PieceEnum getPieceType(){return pieceType;}
}
