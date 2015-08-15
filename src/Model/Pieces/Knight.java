package Model.Pieces;

import Model.ChessModel;
import Model.Position;

import java.util.ArrayList;

/**
 * Created by Adam Fowles on 7/17/2015.
 */
public class Knight extends Piece
{
    private final int[][] offsets = {{1,2},{1,-2},{2,1},{2,-1},{-1,2},{-1,-2},{-2,1},{-2,-1}};
    public Knight(int row, int col, int color, ChessModel model) {super(row, col, color, model);}

    @Override
    public ArrayList<Position> getValidMoves()
    {
        ArrayList<Position> validMoves = new ArrayList<Position>();
        int currentRow = currentPos.row;
        int currentCol = currentPos.col;
        for (int[] offset: offsets)
        {
            if (isValid(offset,currentRow,currentCol))
            {
                validMoves.add(new Position(currentRow + offset[0], currentCol + offset[1]));
            }
        }
        return validMoves;
    }

    private boolean isValid(int[] offset, int currentRow, int currentCol)
    {

        int checkRow = currentRow + offset[0];
        int checkCol = currentCol + offset[1];
        Piece checkPiece = model.getPieceAt(checkRow,checkCol);
        if ( checkPiece != null && (checkPiece.isEmpty || checkPiece.color != this.color))
        {
            return true;
        }
        return false;
    }
}
