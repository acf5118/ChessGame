package Model.Pieces;

import Model.Position;
import Model.ChessModel;
import java.util.ArrayList;

/**
 * Created by Adam Fowles on 7/17/2015.
 */
public class Pawn extends Piece
{
    public Pawn(int row, int col, int color, ChessModel model)
    {
        super(row, col, color, model);
        pieceName = "Pawn";
    }

    @Override
    public ArrayList<Position> getValidMoves()
    {
        // If the pawn is white it is moving upward -1 offset with 6 as a start row
        // otherwise it is moving downward 1 offset with 1 as a start row
        return this.color == WHITE ? getValidMoves(-1, 6): getValidMoves(1, 1);
    }

    /**
     * Gets valid moves based on pawn color
     * @param offset - whether to move up or down, white moves up the rows
     *                 black moves down the rows
     * @param startRow - the start row for a pawn of black or white
     * @return the set of valid moves
     */
    public ArrayList<Position> getValidMoves(int offset, int startRow)
    {
        ArrayList<Position> validMoves = new ArrayList<Position>();
        int currentRow = currentPos.row;
        int currentCol = currentPos.col;
        System.out.println("Row: " + currentRow + " Col: " + currentCol);
        System.out.println("Offset: " + offset);
        // If there is not a piece blocking you
        if (model.getPieceAt(currentRow + offset, currentCol) == null)
        {
            validMoves.add(new Position(currentRow + offset, currentCol));
        }
        // Can move forward two at the start [given same conditions above]
        if (currentRow == startRow &&
                (model.getPieceAt(currentRow + offset, currentCol) == null) &&
                (model.getPieceAt(currentRow + offset*2, currentCol) == null))
        {
            validMoves.add(new Position(currentRow + offset*2, currentCol));
        }
        // Attacking
        Piece leftAdjacent = model.getPieceAt(currentRow + offset, currentCol + offset);
        Piece rightAdjacent = model.getPieceAt(currentRow + offset, currentCol - offset);

        if ((leftAdjacent != null) && leftAdjacent.color != this.color)
        {
            validMoves.add(new Position(currentRow + offset, currentCol + offset));
        }
        if ((rightAdjacent != null) && rightAdjacent.color != this.color)
        {
            validMoves.add(new Position(currentRow + offset, currentCol - offset));
        }
        //TODO enpassent
        //TODO swapping
        //TODO check for 'Check' before allowing a move
        return validMoves;
    }


}
