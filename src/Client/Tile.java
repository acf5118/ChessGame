package Client;

/**
 * Created by Adam Fowles on 8/17/2015.
 */

import Model.PieceEnum;
import Model.Position;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class to represent a tile on
 * the game board.
 * @author Adam Fowles
 */
public class Tile extends JButton
{
    // The tiles position
    public Position position;
    // Whether or not the tile has a piece
    private boolean hasPiece;
    // The chess piece this tile has
    private PieceEnum piece;
    private int pieceColor;
    private ChessView board;
    private Border borderSelected, borderNotSelected;

    public Tile(int color, ChessView cv, int row, int col, Border b)
    {
        hasPiece = false;
        position = new Position(row, col);
        board = cv;
        borderNotSelected = getBorder();
        borderSelected = b;
        if (color == 0)
        {
            setBackground(new Color(255,222,173));
        }
        else
        {
            setBackground(new Color(255,165,0));
        }
        addActionListener(new TileActionListener());
    }

    /**
     * The action listener that handles all clicking actions related to tiles on the board
     * @author Adam Fowles
     *
     */
    private class TileActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            // If it is not your turn (this chess views turn)
            // do nothing.
            if (!board.isTurn()) {return;}
            // If the tile has a piece that is your color
            if (hasPiece && (pieceColor == board.getId()))
            {
                // Don't recalculate valid moves for a piece that is already selected
                if(board.setPieceSelected(Tile.this))
                {
                    board.calculateValidMoves(Tile.this);
                    setBorder(borderSelected);
                }
                return;
            }
            if (board.isPieceClicked())
            {
                // At this point you are trying to make a second move,
                // try to make that move.
                board.validateMove(Tile.this);

            }
        }
    }

    /**
     * Method to visually put a piece on a tile
     * by assigning the buttons image to be a chess piece.
     * @param p - the type of piece
     * @param pc - the color of the piece
     */
    public void putPiece(PieceEnum p, int pc)
    {
        pieceColor = pc;
        String player = pc == 0 ? "W": "B";
        String r = "";
        switch(p)
        {
            case KING:
                r = "Resources/King-" + player + ".png";
                this.piece = PieceEnum.KING;
                break;
            case QUEEN:
                r = "Resources/Queen-" + player + ".png";
                this.piece = PieceEnum.QUEEN;
                break;
            case ROOK:
                r = "Resources/Rook-" + player + ".png";
                this.piece = PieceEnum.ROOK;
                break;
            case PAWN:
                r = "Resources/Pawn-" + player + ".png";
                this.piece = PieceEnum.PAWN;
                break;
            case BISHOP:
                r = "Resources/Bishop-" + player + ".png";
                this.piece = PieceEnum.BISHOP;
                break;
            case KNIGHT:
                r = "Resources/Knight-" + player + ".png";
                this.piece = PieceEnum.KNIGHT;
                break;
            default:
                // Needs error checking
                break;
        }

        setIcon(new ImageIcon(r));
        hasPiece = true;
    }

    /**
     * Opposite method from putPiece, removes
     * the image from the tile.
     */
    public void removePiece()
    {
        setIcon(null);
        clearBorder();
        piece = null;
        pieceColor = -1;
        hasPiece = false;
    }

    public boolean equals(Tile t)
    {
        return position.equals(t.position);
    }
    // Access methods
    public PieceEnum getPiece() {return piece;}
    public int getPieceColor() {return pieceColor;}
    public void clearBorder(){setBorder(borderNotSelected);}
}