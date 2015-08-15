package Model.Pieces;

import Model.Position;

import java.util.ArrayList;

/**
 * Created by Adam Fowles on 8/15/2015.
 */
public class Empty extends Piece
{
    public Empty(int row, int col) {super(row,col);}
    // This method is not used
    public ArrayList<Position> getValidMoves(){return null;}

}
