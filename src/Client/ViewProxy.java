package Client;

import Model.ChessModel;
import Model.ModelListener;
import Model.Position;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The Client.ViewProxy for the server
 * Sends messages back to the client
 * @author Adam Fowles
 *
 */
public class ViewProxy implements ModelListener
{

	// Private fields
	private int id;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private boolean done;
	private ViewListener viewListener;


	/**
	 * Class ReaderThread receives messages from the network, decodes them, and
	 * invokes the proper methods to process them.
	 */
	private class ReaderThread extends Thread
	{
		public void run()		
		{
				for (;;)
				{
					String session, s = "";
					
					try {s = in.readLine();}
					catch(IOException e) {s = "";}
					if (s == null)
					{
						//System.out.println("s is null");
                        continue;
					}
					if (!s.equals(""))
					{
					System.out.println(s);
					Byte b = s.getBytes()[0];
					switch (b)
					{
                        // calc [position]
                        // Calculate the valid moves
                        case 'c':
                            Position p3 = new Position(s.substring(5));
                            ((ChessModel)viewListener).getValidMoves(p3.row, p3.col);
                        case 'j':
                            session = s.substring(5);
                            viewListener.join(ViewProxy.this, session);
                            break;
                        case 'v':
                            Position p = new Position(s.substring(6));
                            ((ChessModel)viewListener).validateMove(p);
                            break;
                        default:
                            System.err.println ("Bad message");
                            break;
					}
					if (done)
					{
						break;
					}
					}
				}

			try{socket.close();}
			catch (IOException exc){}
		}
	}

	/**
	 * Constructor for a Client Proxy object
	 * @param socket
	 */
	public ViewProxy(Socket socket) throws IOException
	{
		this.socket = socket;
		out = new PrintWriter(new DataOutputStream (socket.getOutputStream()));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * Sets the view listener which is the sessionManager at first
	 * and then is set to the model when join is called
	 */
	public void setViewListener(ViewListener listener) 
	{
		if (this.viewListener == null)
		{
			this.viewListener = listener;
			new ReaderThread().start();
		}
		else
		{
			this.viewListener = listener;
		}
	}

	/**
	 * The method that sends a message back to the 
	 * specific client that uses this Client Proxy
	 * @param msg the string message to be sent.
	 */
	public synchronized void sendMessage(String msg) 
	{
		System.out.println("Serving Sending: " + msg);
		out.println(msg);
		out.flush();
	}

	/**
	 * Sends the id message, telling the player which id it has
     * in other words which color it is playing as.
	 */
	public synchronized void id()
	{
		sendMessage("id " + Integer.toString(this.id));
	}

	/**
	 * Sends the name message, telling the player the name
	 * the server got or the name of the other player.     
	 */
	public synchronized void name(int id, String name) 
	{
		sendMessage("name " + Integer.toString(id) + " " + name);
	}

	/**
	 * Sends back the scores or one of the players
	 */
	public synchronized void score(int id, int score) 
	{
		String idS = Integer.toString(id);
		String scoreS = Integer.toString(score);
		sendMessage("score " + idS + " " + scoreS);
	}

	/**
	 * Sends back whose turn it is by id
	 */
	public synchronized void turn(int id) 
	{
		sendMessage("turn " + Integer.toString(id));
	}

	/**
	 * Sends back the winner if and when there is one or tie
	 */
	public synchronized void win(int id) 
	{
		sendMessage("win " + Integer.toString(id) + "\n");
	}

	/**
	 * Sends back a quit message if one players quits
	 */
	public synchronized void quit() 
	{
		sendMessage("quit");
	}

	// Access methods
	public void setId(int id) {this.id = id;}
	public int getId() {return this.id;}

	/**
	 * Sends back the move
	 * @param p1 - the starting piece position
	 * @param p2 - the ending piece position
	 */
	public void moved(Position p1, Position p2)
	{
		sendMessage("move " + p1.toString() + " " + p2.toString());
	}

}
