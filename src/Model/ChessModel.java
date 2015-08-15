package Model;

import Client.ViewListener;
import Client.ViewProxy;
import Model.Pieces.*;


import java.util.ArrayList;

/**
 * Model.ChessModel.java
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
public class ChessModel implements ViewListener
{

	// Private fields
	private ModelListener[] players;
	private String[] names;
	private int whoseTurn,winnerId,numPlayers;
	private boolean finished;
	private Piece[][] board;
    // Store the current 'valid moves' to check against when a player
    // clicks a square to move the piece
    private Piece currentPiece;
    private ArrayList<Position> currentValidMoves;
	
	public ChessModel()
	{
		players = new ModelListener[2];
		names = new String[2];
		whoseTurn = 1;
		winnerId = 0;
		finished = false;
		numPlayers = 0;
		board = new Piece[8][8];
	}

	/**
	 * Add a model listener to the model
	 * @param player - the view proxy that acts as a player
     * @param name - the name of that player
	 */
	public synchronized void addModelListener(ViewProxy player, String name) 
	{
		players[player.getId() - 1] = player;
		names[player.getId() - 1] = name;
		numPlayers++;
		if (numPlayers == 2) 
		{
			players[0].id();
			players[1].id();
			startGame();
			System.out.println("model started game");
		}
		for (ModelListener m: players) 
		{
			if (m != null) 
			{
				ViewProxy v = (ViewProxy)m;
				m.name(v.getId(), names[v.getId() - 1]);
			}
		}
	}
	/**
	 * update function, updates the most
	 * used parts of the game (player score etc)
	 */
	public synchronized void update() 
	{
		for (ModelListener p: players) 
		{
			// Send the scores for both players
			// send whose turn it is
			p.turn(whoseTurn);
			//p.turn(whoseTurn);
		}	
	}
	
	/**
	 * Start the game by sending all the info
	 * to both players
	 */
	private synchronized void startGame() 
	{
		players[0].name(2, names[1]);
		players[1].name(1, names[0]);

		// For the model Black (0) will be on top and White (1) on the bottom
		// Add all the pieces for the starting config
		board[0][0] = new Rook(0,0,0, this);
        board[0][1] = new Knight(0,1,0, this);
        board[0][2] = new Bishop(0,2,0, this);
        board[0][3] = new Queen(0,3,0, this);
        board[0][4] = new King(0,4,0, this);
        board[0][5] = new Bishop(0,5,0, this);
        board[0][6] = new Knight(0,6,0, this);
        board[0][7] = new Rook(0,7,0, this);
        for (int i = 0; i < 8; i++)
        {
            board[1][i] = new Pawn(1,i,0, this);
        }

        board[7][0] = new Rook(7,0,1, this);
        board[7][1] = new Knight(7,1,1, this);
        board[7][2] = new Bishop(7,2,1, this);
        board[7][3] = new King(7,3,1, this);
        board[7][4] = new Queen(7,4,1, this);
        board[7][5] = new Bishop(7,5,1, this);
        board[7][6] = new Knight(7,6,1, this);
        board[7][7] = new Rook(7,7,1, this);

        for (int i = 0; i < 8; i++)
        {
            board[6][i] = new Pawn(6,i,1, this);
        }

		update();
	}

    //public ArrayList<Move> get

	/**
	 * When a player clicks a button updates
	 *
	 */
	public synchronized void move(Position start, Position end)
	{
		// which piece the player has selected
		Piece piece = board[start.row][start.col];
		// null for an empty spot
		board[start.row][start.col] = null;
		board[end.row][end.col] = piece;
        piece.setCurrentPos(end.row, end.col);
		
		if (finished())
		{
			for (ModelListener m: players)
			{
			}
			return;
		}

        whoseTurn = 3 - whoseTurn;

		players[0].moved(start, end);
        players[1].moved(start, end);

		update();
	}


	/**
	 * When a new game is requested reset the game state
	 * and call startGame()
	 */
	public synchronized void newGame(ViewProxy player) 
	{
		
		whoseTurn = 1;
		finished = false;
		winnerId = 0;
		startGame();
	}


	/**
	 * Send the quit game message to the other player
	 * (the first player closed his screen and will be terminated
	 * by the server when Client.ViewProxy.process() returns)
	 */
	public void quit(ViewProxy player) 
	{
		if (numPlayers == 2) 
		{
			if (player.getId() == 1) 
			{
				players[1].quit();
			}
			else 
			{
				players[0].quit();
			}
		}
	}

	/**
	 * Check whether the game is over or not
	 * @return true or false for whether the game is over
	 */
	public boolean finished() 
	{
		//TODO check for checkmate
		return finished;
	}
	
	// Unused method from Client.ViewListener interface
	public void join(ViewProxy player, String name) {}
    public Piece[][] getBoard(){return board;}

    /**
     * Get the piece at the row col index
     * @param row - row index
     * @param col - col index
     * @return the chess Piece at that position or null if no piece is there
     */
    public Piece getPieceAt(int row, int col)
    {
        return board[row][col];
    }

    public void getValidMoves(int row, int col)
    {
        System.out.println("Getting Valid");
        if ( board[row][col] != null)
        {
            currentPiece = board[row][col];
            System.out.println(currentPiece.pieceName);
            currentValidMoves = currentPiece.getValidMoves();
            for (Position p : currentValidMoves)
            {
                System.out.println(Position.convertToSingleArray(p));
            }
        }
        else
        {
            System.err.println("Bad valid move index");
        }
    }

    public void validateMove(Position p)
    {
        System.out.println("Validating");
        // It is a valid move
        if (currentValidMoves.contains(p))
        {
            System.out.println("Moves valid");
            move(currentPiece.getCurrentPos(), p);
        }
        else
        {
            System.out.println("Not valid");
            //TODO send some info back to let the player know it wasn't a valid move
        }
    }


}