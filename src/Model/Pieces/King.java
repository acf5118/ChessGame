package Model.Pieces;

import Model.Position;
import Model.ChessModel;

import java.util.ArrayList;

/**
 * Created by Adam Fowles on 7/17/2015.
 */
public class King extends Piece
{
    public King(int row, int col, int color, ChessModel model)
    {
        super(row, col, color, model);
    }

    @Override
    public ArrayList<Position> getValidMoves()
    {
        ArrayList<Position> validMoves = new ArrayList<Position>();
        int currentRow = currentPos.row;
        int currentCol = currentPos.col;
        // King can move one spot in all eight directions given
        // nothing is blocking him (does not account for check)
        // Up
        if (currentRow + 1 <= 7 &&
                model.getPieceAt(currentRow + 1, currentCol) == null)
        {
            validMoves.add(new Position(currentRow + 1, currentCol));
        }
        // Down
        if (0 <= currentRow - 1 &&
                model.getPieceAt(currentRow - 1, currentCol) == null)
        {
            validMoves.add(new Position(currentRow - 1, currentCol));
        }
        //Left
        if (currentCol + 1 <= 7 &&
                model.getPieceAt(currentRow, currentCol + 1) == null)
        {
            validMoves.add(new Position(currentRow, currentCol + 1));
        }
        //Right
        if (0 <= currentCol - 1 &&
                model.getPieceAt(currentRow, currentCol - 1) == null)
        {
            validMoves.add(new Position(currentRow, currentCol + 1));
        }
        
        return new ArrayList<Position>();
    }
}
