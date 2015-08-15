package Client;

import Model.Position;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


/**
 * Client.ChessView.java
 *
 * Version:
 * $Id$
 * 
 * Revision:
 * $Log$
 */

/**
 * @author Adam Fowles
 *
 */
@SuppressWarnings("serial")
public class ChessView extends JFrame 
{

	private class Tile extends JButton
	{
		// Whether or not the tile has a piece
		private boolean hasPiece;
		// The chess piece this tile has
		private PieceEnum piece;
		private int pieceColor;
		private ChessView board;
        // The tiles position
        public Position position;
		
		public Tile(int color, ChessView cv, int row, int col)
		{
			hasPiece = false;
            position = new Position(row, col);
			board = cv;
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
					board.setPieceSelected(Tile.this);
                    board.calculateValidMoves(Tile.this);
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
			hasPiece = false;
		}

        // Access methods
		public PieceEnum getPiece()
		{
			return piece;
		}
		public int getPieceColor()
		{
			return pieceColor;
		}
	}
	
	private Tile[][] board;
	private int id;
	private int player, oppenent;
	private boolean pieceClicked;
	private ChessClient data;
	private PieceEnum pieceSelected;
	private Tile currentTile;
	private HashMap<Integer,Integer> blackToWhite;

	public ChessView(ChessClient cc) 
	{
		
		super("Chess Game");
		LayoutManager layout = new GridLayout(8,8);
		setLayout(layout);
		board = new Tile[8][8];
		pieceClicked = false;
		pieceSelected = null;
		data = cc;

        // Create a quick conversion for black
		blackToWhite = new HashMap<Integer, Integer>();
		int t = 7;
		for (int i = 0; i < 8 ; i++)
		{
			blackToWhite.put(i,t);
			t--;
		}

        int colorSwitch = 0;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (colorSwitch % 2 == 0)
                {
                    board[i][j] = new Tile(0, this, i, j);
                }
                else
                {
                    board[i][j] = new Tile(1, this, i, j);
                }
                add(board[i][j]);
                colorSwitch++;
            }
            // Flip the column sequence
            colorSwitch++;
        }
		
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Resets the game board back to starting config
	 * based on id. Pulling entire starting config from model is
     * unnecessary, in this case model data is duplicated.
	 * @param id (White or Black)
	 */
	public void reset(int id) 
	{
		// Starting Position for king and queen depending on player color
		Position pK, pQ, oK, oQ;
		
		if (id == 0) 
		{
			player = 0; // Player is white
			oppenent = 1; // Player is black
			pK = new Position(7,4);
			pQ = new Position(7,3);
			oQ = new Position(0,3);
			oK = new Position(0,4);
		}
		else 
		{
			player = 1; // Player is black
			oppenent = 0; // Player is white
            pK = new Position(7,3);
            pQ = new Position(7,4);
            oQ = new Position(0,4);
            oK = new Position(0,3);
		}
		
		for (int i = 0; i < 8; i++)
		{
			board[1][i].putPiece(PieceEnum.PAWN, oppenent);
            board[6][i].putPiece(PieceEnum.PAWN, player);
		}

		board[0][0].putPiece(PieceEnum.ROOK, oppenent);
		board[0][1].putPiece(PieceEnum.KNIGHT, oppenent);
		board[0][2].putPiece(PieceEnum.BISHOP, oppenent);
		board[oQ.row][oQ.col].putPiece(PieceEnum.QUEEN, oppenent);
		board[oK.row][oK.col].putPiece(PieceEnum.KING, oppenent);
		board[0][5].putPiece(PieceEnum.BISHOP, oppenent);
		board[0][6].putPiece(PieceEnum.KNIGHT, oppenent);
		board[0][7].putPiece(PieceEnum.ROOK, oppenent);

        board[7][0].putPiece(PieceEnum.ROOK, player);
        board[7][1].putPiece(PieceEnum.KNIGHT, player);
        board[7][2].putPiece(PieceEnum.BISHOP, player);
        board[pQ.row][pQ.col].putPiece(PieceEnum.QUEEN, player);
        board[pK.row][pK.col].putPiece(PieceEnum.KING, player);
        board[7][5].putPiece(PieceEnum.BISHOP, player);
        board[7][6].putPiece(PieceEnum.KNIGHT, player);
        board[7][7].putPiece(PieceEnum.ROOK, player);
	
	}

	public void initializeBoard()
	{
		System.out.println("look");
		// Server sends ID as either player 1 or 2.
		// Client looks for 0 or 1
        // TODO fix that... ^
		id = Integer.parseInt(data.getId()) - 1;
		reset(id);
		this.validate();
	}

	/**
	 * Set the selected piece on the board
	 * @param t - the tile that was clicked
	 */
	public void setPieceSelected(Tile t) 
	{ 
		pieceSelected = t.piece;
		pieceClicked = true;
		currentTile = t;
	}

	/**
	 * Moves a piece on the board. This method is called only
     * when the client receives message from the server.
	 * @param start - the starting position of the piece
	 * @param end - where to move the piece to
	 */
	public void movePiece(Position start, Position end)
	{
		Tile t1, t2;
		// if you are playing white, positions are normal
		if(id == 0)
		{
			t1 = board[start.row][start.col];
			t2 = board[end.row][end.col];
		}
		else
		{
			t1 = board[blackToWhite.get(start.row)][blackToWhite.get(start.col)];
			t2 = board[blackToWhite.get(end.row)][blackToWhite.get(end.col)];
		}

        t2.putPiece(t1.getPiece(),t1.getPieceColor());
        t1.removePiece();
	}


    /**
     * When a piece is clicked that is your color
     * ask the model/server to calculate valid moves
     * so that when an attempted move is made the model can validate.
     * @param t - the tile which contains a piece to have moves validated on
     */
    public void calculateValidMoves(Tile t)
    {
        int r, c;
        Position p;
        if (id == 1)
        {
            r = blackToWhite.get(t.position.row);
            c = blackToWhite.get(t.position.col);
            p = new Position(r, c);
        }
        else
        {
            p = t.position;
        }

        data.calculateValidMoves(Position.convertToSingleArray(p));
    }

    /**
     * Once a piece has been clicked the player can select any other
     * tile that does not have a piece of their color
     * (selecting a tile that is their color after a piece has been
     * selected will simply select the new piece). And the model (server) will validate
     * that move. If the model finds it valid it will let both clients know that I move
     * has been made.
     * @param t - the tile containing a spot to move to
     */
    public void validateMove(Tile t)
    {
        String s, e;
        int r1, r2, c1, c2, start, end;
        if (id == 1)
        {
            r1 = blackToWhite.get(currentTile.position.row);
            c1 = blackToWhite.get(currentTile.position.col);
            r2 = blackToWhite.get(t.position.row);
            c2 = blackToWhite.get(t.position.col);
            Position startPos = new Position(r1, c1);
            Position endPos = new Position(r2, c2);
            start = Position.convertToSingleArray(startPos);
            end = Position.convertToSingleArray(endPos);

        }
        else
        {
            start = Position.convertToSingleArray(currentTile.position);
            end = Position.convertToSingleArray(t.position);
        }

        if (start < 10)
        {
            s = "0" + start;
        }
        else
        {
            s = Integer.toString(start);
        }

        data.validateMove(end);
        currentTile = null;
        pieceClicked = false;
    }

    // Access methods
    public boolean isPieceClicked() {return pieceClicked;}
    public int getPlayer() {return player;}
    public int getId() {return id;}
    public boolean isTurn() {return data.getTurn();}

}
