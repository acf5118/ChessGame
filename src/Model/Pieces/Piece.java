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

    public Piece(int row, int col, int color, ChessModel model)
    {
        this.currentPos = new Position(row, col);
        this.color = color;
        this.model = model;
    }

    public abstract ArrayList<Position> getValidMoves();
    public Position getCurrentPos(){return currentPos;}
    public void setCurrentPos(int row, int col){currentPos = new Position(row, col);}
}
