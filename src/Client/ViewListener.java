package Client;

import Model.Position;

/**
 * Client Listener interface
 * see implemented methods for details
 * (Server.SessionManager, FifteenModel)
 * @author Adam Fowles
 *
 */
public interface ViewListener
{
	public void join(ViewProxy player, String session);
	public void move(Position start, Position end);
	public void newGame(ViewProxy player);
	public void quit(ViewProxy player);
}
