package Client;

import Model.Position;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
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
	private boolean win, turn = false;
	private String winner = "";
	// Initialize fields
	private String playername = "", rival = "waiting for partner",
			id = "", myScore = "", rScore = "", checkDigits = "111111111";
	/**
	 * Helper Class that reads data and updates view
	 */
	private class ReaderThread	extends Thread {

		// Thread will update view
		private ChessView view;
		public ReaderThread(ChessView v) 
		{
			this.view = v;
		}
		public void run()
		{
			System.out.println("started");
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
					// switch on server responses where
					// the case comment is whole the server message
					switch(b) 
					{
					// White or Black 1 for White 2 for Black
					case 'i': // id i
						id = s.substring(3);
						System.out.println("gets to client");
						view.initializeBoard();
						break;
					case 'n': // name i n
						if (s.substring(5, 6).equals(id)) {}
						else 
						{ 
							rival = s.substring(7);
							//view.update();
						}
						break;
					case 't': // turn i
						String t = s.substring(5);
						if (t.equals(id)) { turn = true;}
						else { turn = false;}
						//view.update();
						break;
					case 'm': // move from other player
						int start = Integer.parseInt(s.substring(5,7));
						int end = Integer.parseInt(s.substring(8,10));
						view.movePiece(Position.convertToMultiArray(start),
                                Position.convertToMultiArray(end));
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
	public ChessClient(String playername, String host, int port) {

		this.socket = new Socket();
		this.playername = playername;
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
	 * Try to join the server
	 * @throws IOException
	 */
	public void joinServer() throws IOException {
		out.println("join " + playername);
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
	 * Start the thread to read data from the server
	 */
	public void startReading(ChessView v) {
		ReaderThread read = new ReaderThread(v);
		read.start();
	}

	/**
	 * Accessers
	 */
	public String getId() { return this.id;}
	public String getRival() { return this.rival;}
	public String getMyScore() { return this.myScore;}
	public String getRScore() { return this.rScore;}
	public String getName() { return this.playername;}
	public String getWinnerName() { return this.winner;}
	public String checkNewGame() { return this.checkDigits;}
	public char[] getDigits() { return this.checkDigits.toCharArray();}
	public boolean getWinner() { return this.win;}
	public boolean getTurn() { return this.turn;}

    /**
     * Ask the server to calculate valid moves for the piece
     * at the position on the board
     * @param position - the position of the piece to calculate valid moves for
     */
    public void calculateValidMoves(int position)
    {
        String pos = "";
        if (position < 10)
        {
            pos = "0" + position;
        }
        else
        {
            pos = Integer.toString(position);
        }
        try
        {
            writeOut("calc " + pos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validateMove(int position)
    {
        String s;
        if (position < 10)
        {
            s = "0" + position;
        }
        else
        {
             s = Integer.toString(position);
        }
        try
        {
            writeOut("valid " + s);
        }
        catch (IOException e){e.printStackTrace();}
    }

	/**
	 * resets the game state
	 */
	public void reset() {
		this.checkDigits = "111111111";
		this.win = false;
		this.winner = "";
	}


	/**
	 * main program, checks command line args
	 * creates a chess client object and chess view object
	 * and starts reading data from server
	 * @param args
	 */
	public static void main(String[] args) {

		String playername = args[0];
		String host = args[1];
		int port = 0;
		try{
			port = Integer.parseInt (args[2]);
		} catch (Exception e) {
			System.err.println("Please enter a number for port");
			System.exit(1);
		}
		// Set up socket, input and output
		ChessClient data = new ChessClient(playername,host,port);
		ChessView playerView = new ChessView(data);
		data.startReading(playerView);
		try { data.joinServer();}
		catch (IOException e) { 
			System.err.println(
					"Cannot join server at " + host + Integer.toString(port));
			System.exit(1);}
		// Set up Client GUI

	}

}
