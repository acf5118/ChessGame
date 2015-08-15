package Server;
/**
 * Server.SessionManager.java
 *
 * Version:
 * $Id$
 * 
 * Revision:
 * $Log$
 */
import Model.ChessModel;
import Client.ViewListener;
import Client.ViewProxy;
import Model.Position;

import java.util.HashMap;
/**
 * Class for a Session Manager, implements ViewListner
 * specifically the join method. Once join has been called
 * Session Manager hands off the role of Client.ViewListener to
 * the model. 
 * Functions
 * @author Adam Fowles
 *
 */
public class SessionManager implements ViewListener
{

	// Private fields
	private HashMap<Integer,ChessModel> sessions;
	private int sessionCount;
	
	/**
	 * Constructor for a Server.SessionManager
	 */
	public SessionManager() 
	{
		sessions = new HashMap<Integer, ChessModel>();
		sessionCount = 0;
	}
	
	/**
	 * The join method, sets up the appropriate view listeners
	 * and model listeners
	 */
	public void join(ViewProxy player, String name) 
	{
		System.out.println("this join is getting called");
		ChessModel model = sessions.get(sessionCount);
		if (model == null) 
		{
			model = new ChessModel();
			sessions.put(sessionCount,model);
			player.setId(1);
		}
		else 
		{
			sessionCount += 1;
			player.setId(2);
		}
		// send the player his ID
		player.id();
	    model.addModelListener(player, name);
	    player.setViewListener(model);
	}

	// Unused methods from Client.ViewListener interface
	public void newGame(ViewProxy player) {}
	public void quit(ViewProxy player) {}
	public void move(Position start, Position end) {}

}