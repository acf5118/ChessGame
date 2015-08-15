package Model; /**
 * Model.ModelListener.java
 *
 * Version:
 * $Id$
 * 
 * Revision:
 * $Log$
 */

/**
 * The interface for a Model.ModelListener
 * See implemented methods for details
 * (Client.ViewProxy)
 * @author Adam Fowles
 *
 */
public interface ModelListener
{
	public void id();
	public void name(int id, String name);
	public void moved(Position start, Position end);
	public void score(int id, int score);
	public void turn(int turn);
	public void win(int id);
	public void quit();	
}
