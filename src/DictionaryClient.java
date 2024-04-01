import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONObject;

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
			System.out.println("GUI");
		}
	}

	/**
	 * Search the meaning of a word in the dictionary.
	 * @param word client input
	 * @return meaning of the word
	 */
	public String search(String word) {
		try(Socket socket = new Socket(serverAddress, port);) {
			System.out.println("Connection established");

			// get the input/output streams for reading/writing data from/to the socket
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			// generate message to server
			JSONObject message = new JSONObject();
			message.put("command", "search");
			message.put("word", word);

		    // send message to server
	    	output.writeUTF(message.toString());
	    	System.out.println("Data sent to Server--> " + message.toString());
	    	output.flush();
			System.out.println("Message sent");

			// receive response from server
			String response = input.readUTF();
			System.out.println(response);
			JSONObject responseJson = new JSONObject(response);
			String meaning = responseJson.getString("meaning");
			return meaning;
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return "failed";
	}

	/**
	 * Add a new word and the meaning to the dictionary.
	 * @param word word to be added
	 * @param meaning meaning to be added
	 * @return whether the word and its meaning are successfully added to the dictionary
	 */
	public boolean add(String word, String meaning) {
		try(Socket socket = new Socket(serverAddress, port);) {
			System.out.println("Connection established");

			// get the input/output streams for reading/writing data from/to the socket
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			// generate message to server
			JSONObject message = new JSONObject();
			message.put("command", "add");
			message.put("word", word);
			message.put("meaning", meaning);

			// send message to server
			output.writeUTF(message.toString());
			System.out.println("Data sent to Server--> " + message.toString());
			output.flush();
			System.out.println("Message sent");

			// receive response from server
			String response = input.readUTF();
			System.out.println(response);
			JSONObject responseJson = new JSONObject(response);
			if (responseJson.getString("status").equals("Successful")) {
				return true;
			} else {
				return false;
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
		return false;
	}

	/**
	 * Remove a word and all its meanings from the dictionary.
	 * @param word word to be removed
	 * @return whether the word is successfully removed from the dictionary
	 */
	public boolean remove(String word) {
		try(Socket socket = new Socket(serverAddress, port);) {
			System.out.println("Connection established");

			// get the input/output streams for reading/writing data from/to the socket
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			// generate message to server
			JSONObject message = new JSONObject();
			message.put("command", "remove");
			message.put("word", word);

			// send message to server
			output.writeUTF(message.toString());
			System.out.println("Data sent to Server--> " + message.toString());
			output.flush();
			System.out.println("Message sent");

			// receive response from server
			String response = input.readUTF();
			System.out.println(response);
			JSONObject responseJson = new JSONObject(response);
			if (responseJson.getString("status").equals("Successful")) {
				return true;
			} else {
				return false;
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
		return false;
	}

	/**
	 * Update a word and its meanings in the dictionary.
	 * @param word word to be updated
	 * @param meaning meaning to be updated
	 * @return whether the update is successful
	 */
	public boolean update(String word, String meaning) {
		try(Socket socket = new Socket(serverAddress, port);) {
			System.out.println("Connection established");

			// get the input/output streams for reading/writing data from/to the socket
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			// generate message to server
			JSONObject message = new JSONObject();
			message.put("command", "update");
			message.put("word", word);
			message.put("meaning", meaning);

			// send message to server
			output.writeUTF(message.toString());
			System.out.println("Data sent to Server--> " + message.toString());
			output.flush();
			System.out.println("Message sent");

			// receive response from server
			String response = input.readUTF();
			System.out.println(response);
			JSONObject responseJson = new JSONObject(response);
			if (responseJson.getString("status").equals("Successful")) {
				return true;
			} else {
				return false;
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
		return false;
	}

}
