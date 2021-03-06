package Server;

import Client.ViewProxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Chess Server Class
 * listens on the port for connections and passes them
 * off to a view proxy. Initially the view proxy is attached
 * to the session manager until the client calls join, at which
 * point the view proxy is attached to the chess model.
 * @author Adam Fowles
 *
 */
public class ChessServer
{
	/**
	 * Main program that runs the server
	 * @param args: arg0 host, arg1 port
	 * @throws IOException hopefully this is never thrown
	 * since there will be nothing to catch the error
	 */
	public static void main (String[] args) throws IOException
    {

		if (args.length != 2) {
			System.err.println("Usage: java Server.ChessServer host port");
			System.exit(1);
		}
		String host = args[0];
		int port = Integer.parseInt (args[1]);

		@SuppressWarnings("resource")
		ServerSocket serversocket = new ServerSocket();
		serversocket.bind (new InetSocketAddress (host, port));

		SessionManager manager = new SessionManager();

		for (;;)
		{
			Socket socket = serversocket.accept();
			ViewProxy proxy = null;
			try{
				proxy = new ViewProxy(socket);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			if (proxy != null)
			{
				proxy.setViewListener(manager);
			}
		}
	}

}
