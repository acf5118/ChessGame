import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Client.ChessClient;
import Client.ChessView;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * ChessStart.java
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
public class ChessStart extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JTextField host;
	private JTextField port;
	private JLabel lblPlayerName;
	private JTextField playerName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChessStart frame = new ChessStart();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChessStart() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 274, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblServer = new JLabel("Server");
		lblServer.setBounds(10, 11, 46, 14);
		panel.add(lblServer);
		
		JLabel lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(10, 36, 69, 14);
		panel.add(lblIpAddress);
		
		host = new JTextField();
		host.setBounds(89, 33, 86, 20);
		panel.add(host);
		host.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(10, 61, 46, 14);
		panel.add(lblPort);
		
		port = new JTextField();
		port.setBounds(89, 58, 86, 20);
		panel.add(port);
		port.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(86, 147, 89, 23);
		panel.add(btnConnect);
		btnConnect.addActionListener(new ConnectActionListener());
		
		lblPlayerName = new JLabel("Player Name:");
		lblPlayerName.setBounds(10, 86, 69, 14);
		panel.add(lblPlayerName);
		
		playerName = new JTextField();
		playerName.setBounds(89, 83, 86, 20);
		panel.add(playerName);
		playerName.setColumns(10);
		
	}
	
	private class ConnectActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			String playername = playerName.getText();
			String host_address = host.getText();
			int port_num = 0;
			try{
				port_num = Integer.parseInt (port.getText());
			} catch (Exception e) {
				System.err.println("Please enter a number for port");
				System.exit(1);
			}
			// Set up socket, input and output
			ChessClient data = new ChessClient(playername,host_address,port_num);
			try { data.joinServer();}
			catch (IOException e) { 
				System.err.println(
						"Cannot join server at " + host + Integer.toString(port_num));
				System.exit(1);}
			// Set up Client GUI
			ChessView playerView = new ChessView(data);
			data.startReading(playerView);

			
		}
		
	}
}
