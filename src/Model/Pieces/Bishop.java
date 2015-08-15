package Model.Pieces;

import Model.ChessModel;
import Model.Position;

import java.util.ArrayList;

/**
 * Created by Adam Fowles on 7/17/2015.
 */
public class Bishop extends Piece
{
    public Bishop(int row, int col, int color, ChessModel model)
    {
        super(row, col, color, model);
    }

    @Override
    public ArrayList<Position> getValidMoves()
    {
        return new ArrayList<Position>();
    }
}
