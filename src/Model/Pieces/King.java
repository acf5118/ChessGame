package Model.Pieces;

import Model.Position;
import Model.ChessModel;

import java.util.ArrayList;

/**
 * Created by Adam Fowles on 7/17/2015.
 */
public class King extends Piece
{
    //                                {Row, Col}
    private final int[][] offsets = {{0,1},{0,-1},{1,0},{1,1},{1,-1},{-1,0},{-1,1},{-1,-1}};
    public boolean hasMoved;
    public King(int row, int col, int color, ChessModel model)
    {
        super(row, col, color, model);
        hasMoved = false;
    }

    @Override
    public ArrayList<Position> getValidMoves()
    {
        ArrayList<Position> validMoves = new ArrayList<Position>();
        int currentRow = currentPos.row;
        int currentCol = currentPos.col;

        for (int[] offset : offsets)
        {
            if (isValid(offset,currentRow,currentCol))
            {
                validMoves.add(new Position(currentRow + offset[0],currentCol + offset[1]));
            }
        }
        return validMoves;
    }

    private boolean isValid(int[] offset, int currentRow, int currentCol)
    {
        // King can move one spot in all eight directions given
        // nothing is blocking him (does not account for check)
        // Up
        int checkRow = currentRow + offset[0];
        int checkCol = currentCol + offset[1];
        Piece checkPiece = model.getPieceAt(checkRow,checkCol);
        if ( checkPiece != null && (checkPiece.isEmpty || checkPiece.color != this.color))
        {
            return true;
        }
        //TODO Castle
        return false;
    }
}
