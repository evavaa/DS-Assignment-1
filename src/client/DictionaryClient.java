package client;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class DictionaryClient {
	
	// server address and port
	private static int port;
	private static String serverAddress;
	private static ClientGUI clientGUI;

	public static void main(String[] args) {
		try {
			// read server address and port number from the command line
			serverAddress = args[0];
			port = Integer.parseInt(args[1]);
			// launch the application
			clientGUI = new ClientGUI();
		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null, "Please enter server address and port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
			//TODO: for testing, delete later
			port = 30005;
			serverAddress = "localhost";
			clientGUI = new ClientGUI();
		}
	}

	/**
	 * Search the meaning of a word in the dictionary.
	 * @param word client input
	 * @return meaning of the word
	 */
	public String search(String word) {
		try(Socket socket = new Socket(serverAddress, port);)
		{
			// Output and Input Stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		    String sendData ="I want to connect";
		    
	    	output.writeUTF(sendData);
	    	System.out.println("Data sent to Server--> " + sendData);
	    	output.flush();
	    	
	    	boolean flag=true;
		    while(flag)
		    {
		    	if(input.available()>0) {
		    		String message = input.readUTF();
		    		System.out.println(message);
		    		flag= false;;
		    	}
		    }
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return "done";
	}

	/**
	 * Add a new word and the meaning to the dictionary.
	 * @param word word to be added
	 * @param meaning meaning to be added
	 * @return whether the word and its meaning are successfully added to the dictionary
	 */
	public boolean add(String word, String meaning) {
		try(Socket socket = new Socket(serverAddress, port);)
		{
			// Output and Input Stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String sendData ="I want to connect";

			output.writeUTF(sendData);
			System.out.println("Data sent to Server--> " + sendData);
			output.flush();

			boolean flag=true;
			while(flag)
			{
				if(input.available()>0) {
					String message = input.readUTF();
					System.out.println(message);
					flag= false;;
				}
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Remove a word and all its meanings from the dictionary.
	 * @param word word to be removed
	 * @return whether the word is successfully removed from the dictionary
	 */
	public boolean remove(String word) {
		try(Socket socket = new Socket(serverAddress, port);)
		{
			// Output and Input Stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String sendData ="I want to connect";

			output.writeUTF(sendData);
			System.out.println("Data sent to Server--> " + sendData);
			output.flush();

			boolean flag=true;
			while(flag)
			{
				if(input.available()>0) {
					String message = input.readUTF();
					System.out.println(message);
					flag= false;;
				}
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Update a word and its meanings in the dictionary.
	 * @param word word to be updated
	 * @param meaning meaning to be updated
	 * @return whether the update is successful
	 */
	public boolean update(String word, String meaning) {
		try(Socket socket = new Socket(serverAddress, port);)
		{
			// Output and Input Stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String sendData ="I want to connect";

			output.writeUTF(sendData);
			System.out.println("Data sent to Server--> " + sendData);
			output.flush();

			boolean flag=true;
			while(flag)
			{
				if(input.available()>0) {
					String message = input.readUTF();
					System.out.println(message);
					flag= false;;
				}
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

}
