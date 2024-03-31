package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.swing.*;

public class DictionaryServer {
	
	// Declare the port number
	private static int port;
	private static String dictionaryFile;
	
	// Identifies the user number connected
	private static int counter = 0;

	public static void main(String[] args)
	{
		// read port number and dictionary file from the command line
		try {
			port = Integer.parseInt(args[0]);
			dictionaryFile = args[1];

		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Please enter the 'Port number' and 'Dictionary file path'.", "Server Error", JOptionPane.ERROR_MESSAGE);
		}
		port = 30005;

		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try(ServerSocket server = factory.createServerSocket(port))
		{
			System.out.println("Waiting for client connection-");
			
			// Wait for connections.
			while(true)
			{
				Socket client = server.accept();
				counter++;
				System.out.println("Client "+counter+": Applying for connection!");
							
				// Start a new thread for a connection
				Thread t = new Thread(() -> serveClient(client));
				t.start();
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static void serveClient(Socket client)
	{
		try(Socket clientSocket = client)
		{
			// Input stream
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
			// Output Stream
		    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
		    
		    System.out.println("CLIENT: "+input.readUTF());
		    
		    output.writeUTF("Server: Hi Client "+counter+" !!!");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
