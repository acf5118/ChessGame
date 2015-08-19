package Client;

import Model.Position;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


/**
 * Main class for Chess Client Program
 * Creates a client and connects it to a server
 * Creates a view and all client interaction. 
 * @author Adam Fowles
 */
public class ChessClient 
{

	// Fields for a Client.ChessClient instance
	private PrintStream out;
	private Scanner in;
	private Socket socket;
	private boolean turn = false;
	// Initialize fields
	private String playerName = "", rival = "waiting for partner", id = "";
	/**
	 * Helper Class that reads data and updates view
	 */
	private class ReaderThread extends Thread
    {
		// Thread will update view
		private ChessView view;
		public ReaderThread(ChessView v) 
		{
			this.view = v;
		}
		public void run()
		{
			try
			{
				for (;;)
				{
					if (!in.hasNextLine())
					{
						continue;
					}
					String s = in.nextLine().replace("\n", "");
					System.out.println("Server to Client: " + s);
					if (s.equals(""))
					{
						continue;
					}
					byte b = s.getBytes()[0];
					// switch on server responses (where
					// the case java comment is whole the server message)
					switch(b) 
					{
					    // White or Black 1 for White 2 for Black
                        case 'i': // id i
                            id = s.substring(3);
                            view.initializeBoard();
                            break;
                        case 'n': // name i n
                            if (!s.substring(5, 6).equals(id))
                            {
                                rival = s.substring(7);
                                //view.update();
                            }
                            break;
                        case 't': // turn i
                            String t = s.substring(5);
                            turn = t.equals(id);
                            //view.update();
                            break;
                        // Move a piece
                        case 'm': // move ## ##
                            Position start = new Position(s.substring(5,10));
                            Position end = new Position(s.substring(11));
                            view.movePiece(start, end);
                            break;
					}
				}
			}//try
			finally
            {
				try{socket.close();}
				catch (IOException exc){}
			}
		} //run()
	} // ReaderThread

	/**
	 * Constructor for an instance of Chess 
	 * which contains all the data to be passed
	 * into Chess Client, acts as the model.
	 */
	public ChessClient(String playerName, String host, int port)
    {

		this.socket = new Socket();
		this.playerName = playerName;
		// Connect to a new socket or catch the IO exception that is thrown
		try {socket.connect (new InetSocketAddress (host, port));} 
		catch (IOException e) {
			System.err.println("Cannot connect to " + host);
			System.exit(1);}
		// Connect the data streams in and out
		try { out = new PrintStream (socket.getOutputStream());} 
		catch (IOException e) {
			System.err.println("Cannot open output stream");
			System.exit(1);}
		try { in = new Scanner (socket.getInputStream());} 
		catch (IOException e) { 
			System.err.println("Cannot open input stream");
			System.exit(1);}

	}

    /**
     * A method to write data out to the server
     * @param s - the string to write
     * @throws IOException
     */
    public void writeOut(String s) throws IOException
    {
        System.out.println("Client Sending Server: " + s);
        out.println(s);
    }

	/**
	 * Try to join the server
	 * @throws IOException
	 */
	public void joinServer() throws IOException {writeOut("join " + playerName);}

    /**
     * Start reading data from the server
     * @param v - the chess view to update with server messages
     */
	public void startReading(ChessView v)
    {
		ReaderThread read = new ReaderThread(v);
		read.start();
	}

	// Access methods
	public String getId() { return this.id;}
	public boolean getTurn() { return this.turn;}

    /**
     * Ask the server to calculate valid moves for the piece
     * at the position on the board
     * @param position - the position of the piece to calculate valid moves for
     */
    public void calculateValidMoves(Position position)
    {
        try
        {
            writeOut("calc " + position.toString());
        }
        catch (IOException e) {e.printStackTrace();}
    }

    /**
     * Ask the server to validate the move.
     * Only sends the server the end position since it knows the starting
     * position already from calling the method calculateValidMoves.
     * This method will not get called unless calculateValidMoves has already
     * been called.
     * @param position - the position to move to
     */
    public void validateMove(Position position)
    {
        try
        {
            writeOut("valid " + position.toString());
        }
        catch (IOException e){e.printStackTrace();}
    }

	/**
	 * main program, checks command line args
	 * creates a chess client object and chess view object
	 * and starts reading data from server
	 * @param args - [player name], [host], [port]
	 */
	public static void main(String[] args)
    {

		String playerName = args[0];
		String host = args[1];
		int port = 0;
		try{
			port = Integer.parseInt (args[2]);
		} catch (Exception e) {
			System.err.println("Please enter a number for port");
			System.exit(1);
		}
		// Set up socket, input and output
		ChessClient data = new ChessClient(playerName,host,port);
		ChessView playerView = new ChessView(data);
		data.startReading(playerView);
		try { data.joinServer();}
		catch (IOException e) { 
			System.err.println(
					"Cannot join server at " + host + Integer.toString(port));
			System.exit(1);}
	}

}
