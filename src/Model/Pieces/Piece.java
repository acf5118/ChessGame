package Model.Pieces; /**
 * Model.Pieces.Model.Pieces.java
 *
 * Version:
 * $Id$
 * 
 * Revision:
 * $Log$
 */

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
    public String pieceName = "";
    public boolean isEmpty;

    public Piece(int row, int col, int color, ChessModel model)
    {
        this.currentPos = new Position(row, col);
        this.color = color;
        this.model = model;
        isEmpty = false;
    }

    public Piece(int row, int col){isEmpty = true;}

    /**
     * Gets the valid moves for a piece that can move multiple positions
     * @param rowOffset - up or down the row
     * @param colOffset - up or down the column
     * @param currentCol - current column position of the piece
     * @param currentRow - current row position of the piece
     * @return a list of positions
     */
    protected ArrayList<Position> getValidMultiple(int rowOffset, int colOffset,
                                                        int currentRow, int currentCol)
    {
        System.out.println("Current Row: " + currentRow + "Current Col: " + currentCol);
        ArrayList<Position> validMoves = new ArrayList<Position>();
        int row = currentRow + rowOffset;
        int col = currentCol + colOffset;
        while (model.getPieceAt(row,col) != null)
        {
            System.out.println("row: " + row + "col: " + col);
            if (model.getPieceAt(row, col).isEmpty)
            {
                validMoves.add(new Position(row, col));
            }
            else if (model.getPieceAt(row, col).color != this.color)
            {
                validMoves.add(new Position(row,col));
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

    public abstract ArrayList<Position> getValidMoves();
    public Position getCurrentPos(){return currentPos;}
    public void setCurrentPos(int row, int col){currentPos = new Position(row, col);}
}
