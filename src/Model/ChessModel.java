package Model;

import Client.ViewListener;
import Client.ViewProxy;
import Model.Pieces.*;

import java.util.ArrayList;

/**
 * @author Adam Fowles
 *
 */
public class ChessModel implements ViewListener
{
    private ModelListener[] players;
	private String[] names;
	private int whoseTurn,winnerId,numPlayers;
	private boolean finished;
	private Piece[][] board;
    private King[] kings;
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
        kings = new King[2];
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

        kings[0] = new King(0,4,0, this);
        board[0][4] = kings[0];
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
        board[7][3] = new Queen(7,3,1, this);
        kings[1] = new King(7,4,1, this);
        board[7][4] = kings[1];
        board[7][5] = new Bishop(7,5,1, this);
        board[7][6] = new Knight(7,6,1, this);
        board[7][7] = new Rook(7,7,1, this);

        for (int i = 0; i < 8; i++)
        {
            board[6][i] = new Pawn(6,i,1, this);
        }

        for (int i = 2; i < 6; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j] = new Empty(i,j);
            }
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
		board[start.row][start.col] = new Empty(start.row,start.col);
		board[end.row][end.col] = piece;
        piece.setCurrentPos(end.row, end.col);
        // If the other players king is in check
        if (inCheck(kings[1 - piece.color]))
        {
            System.out.println("Other play is in check");
            //TODO let the player know
        }
        // if your king is in check move is invalid still
        if (inCheck(kings[piece.color]))
        {
            System.out.println("check king invalid");
            // undo everything
            board[start.row][start.col] = piece;
            board[end.row][end.col] = new Empty(end.row,end.col);
            piece.setCurrentPos(start.row, start.col);
            return;
        }
        piece.hasMoved = true;

        // Castling
        if (piece.getPieceType() == PieceEnum.KING &&
                (Math.abs(start.col - end.col) == 2))
        {
            Piece rook;
            int col = end.col < start.col ? 0: 7;
            int offset = end.col < start.col ? 1: -1;

            rook = board[start.row][col];
            board[start.row][col] = new Empty(start.row, col);
            board[start.row][end.col + offset] = rook;
            rook.setCurrentPos(start.row, end.col + offset);
            rook.hasMoved = true;
            Position startRook = new Position(start.row, col);
            Position endRook = new Position(start.row, end.col + offset);
            players[0].moved(startRook, endRook);
            players[1].moved(startRook, endRook);

        }

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
     * @return the chess Piece at that position or null if the spot is outside the board
     */
    public Piece getPieceAt(int row, int col)
    {
        if (row < 0 || row > 7 || col < 0 || col > 7)
        {
            return null;
        }
        return board[row][col];
    }

    public void getValidMoves(int row, int col)
    {
        System.out.println("Getting Valid");
        // It is not possible for this to return null since any
        // square a user clicks will have proper row and col values
        if (!getPieceAt(row, col).isEmpty)
        {
            currentPiece = board[row][col];
            currentValidMoves = currentPiece.getValidMoves();
            for (Position p : currentValidMoves)
            {
                System.out.println(p.toString());
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

    public boolean inCheck(King k)
    {
        int numCheck = 0;
        for (Piece[] col: board)
        {
            for (Piece p: col)
            {
                if (!p.isEmpty && p.color != k.color)
                {
                    if(p.getValidMoves().contains(k.getCurrentPos()))
                    {
                        numCheck++;
                        System.out.println("King in check");
                    }
                }
            }
        }
        return numCheck != 0;
    }


}