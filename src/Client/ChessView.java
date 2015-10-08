package Client;

import Model.PieceEnum;
import Model.Position;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * The View for playing the Chess Game
 * Displays the game state and acts as the controller
 * for the user.
 * @author Adam Fowles
 */
@SuppressWarnings("serial")
public class ChessView extends JFrame 
{
	private Tile[][] board;
	private int id;
	private int player, opponent;
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

        Color c = new Color(255, 252, 31);
        Border b = BorderFactory.createLineBorder(c,2);
        int colorSwitch = 0;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (colorSwitch % 2 == 0)
                {
                    board[i][j] = new Tile(0, this, i, j, b);
                }
                else
                {
                    board[i][j] = new Tile(1, this, i, j, b);
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
            opponent = 1; // Player is black
			pK = new Position(7,4);
			pQ = new Position(7,3);
			oQ = new Position(0,3);
			oK = new Position(0,4);
		}
		else 
		{
			player = 1; // Player is black
            opponent = 0; // Player is white
            pK = new Position(7,3);
            pQ = new Position(7,4);
            oQ = new Position(0,4);
            oK = new Position(0,3);
		}
		
		for (int i = 0; i < 8; i++)
		{
			board[1][i].putPiece(PieceEnum.PAWN, opponent);
            board[6][i].putPiece(PieceEnum.PAWN, player);
		}

		board[0][0].putPiece(PieceEnum.ROOK, opponent);
		board[0][1].putPiece(PieceEnum.KNIGHT, opponent);
		board[0][2].putPiece(PieceEnum.BISHOP, opponent);
		board[oQ.row][oQ.col].putPiece(PieceEnum.QUEEN, opponent);
		board[oK.row][oK.col].putPiece(PieceEnum.KING, opponent);
		board[0][5].putPiece(PieceEnum.BISHOP, opponent);
		board[0][6].putPiece(PieceEnum.KNIGHT, opponent);
		board[0][7].putPiece(PieceEnum.ROOK, opponent);

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
	public boolean setPieceSelected(Tile t)
	{
        if (currentTile != null)
        {
            if(currentTile.equals(t)){return false;}
            currentTile.clearBorder();
        }
		pieceSelected = t.getPiece();
		pieceClicked = true;
		currentTile = t;
        return true;
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
        // Player is playing as Black
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

        data.calculateValidMoves(p);
    }

    /**
     * Once a piece has been clicked the player can select any other
     * tile that does not have a piece of their color
     * (selecting a tile that is their color after a piece has been
     * selected will simply select the new piece). And the model (server) will validate
     * that move. If the model finds it valid it will let both clients know that a move
     * has been made.
     * @param t - the tile containing a spot to move to
     */
    public void validateMove(Tile t)
    {
        Position p;
        int row, col;
        // If the player is playing as black
        if (id == 1)
        {
            row = blackToWhite.get(t.position.row);
            col = blackToWhite.get(t.position.col);
            p = new Position(row, col);
        }
        else
        {
            p = t.position;
        }

        data.validateMove(p);
        currentTile.clearBorder();
        currentTile = null;
        pieceClicked = false;
    }

    // Access methods
    public boolean isPieceClicked() {return pieceClicked;}
    public int getId() {return id;}
    public boolean isTurn() {return data.getTurn();}

}
