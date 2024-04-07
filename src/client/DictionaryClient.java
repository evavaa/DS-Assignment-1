package client;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

import javax.swing.*;

public class DictionaryClient {
	
	// server address and port
	private static int port;
	private static String serverAddress;

	public static void main(String[] args) {
		try {
			// read server address and port number from the command line
			serverAddress = args[0];
			port = Integer.parseInt(args[1]);

			// launch the application
			ClientGUI clientGUI = new ClientGUI();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Please provide server address and port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Send the request to server and get the response.
	 * thread-per-request
	 * @param message request to be sent to the server
	 * @return response received from server
	 */
	private String sendRequest(String message) {
		try(Socket socket = new Socket(serverAddress, port)) {
			System.out.println("Connection established");

			// get the input/output streams for reading/writing data from/to the socket
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			// send message to server
			output.writeUTF(message);
			System.out.println("Data sent to Server--> " + message);
			output.flush();
			System.out.println("Message sent");

			// receive response from server
			String response = input.readUTF();
			System.out.println(response);

			output.close();
			input.close();
			socket.close();

			return response;

		} catch (IOException e)
		{
			// graceful exit
			int reply = JOptionPane.showConfirmDialog(null, "The server is currently down. Do you want to exit the application?", "Server Error",  JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}
			e.printStackTrace();
		}
		return "Error";
	}

	/**
	 * Search the meaning of a word in the dictionary.
	 * @param word client input
	 * @return meaning of the word
	 */
	public String search(String word) {
		// generate message to server
		JSONObject message = new JSONObject();
		message.put("command", "search");
		message.put("word", word);

		// send the request to server and get the response
		String response = sendRequest(message.toString());
		JSONObject responseJson = new JSONObject(response);
		return responseJson.getString("meaning");
	}

	/**
	 * Add a new word and the meaning to the dictionary.
	 * @param word word to be added
	 * @param meaning meaning to be added
	 * @return whether the word and its meaning are successfully added to the dictionary
	 */
	public boolean add(String word, String meaning) {
		// generate message to server
		JSONObject message = new JSONObject();
		message.put("command", "add");
		message.put("word", word);
		message.put("meaning", meaning);

		// send the request to server and get the response
		String response = sendRequest(message.toString());
		JSONObject responseJson = new JSONObject(response);
		return responseJson.getString("status").equals("Successful");
	}

	/**
	 * Remove a word and all its meanings from the dictionary.
	 * @param word word to be removed
	 * @return whether the word is successfully removed from the dictionary
	 */
	public boolean remove(String word) {
		// generate message to server
		JSONObject message = new JSONObject();
		message.put("command", "remove");
		message.put("word", word);

		// send the request to server and get the response
		String response = sendRequest(message.toString());
		JSONObject responseJson = new JSONObject(response);
		return responseJson.getString("status").equals("Successful");
	}

	/**
	 * Update a word and its meanings in the dictionary.
	 * @param word word to be updated
	 * @param meaning meaning to be updated
	 * @return whether the update is successful
	 */
	public boolean update(String word, String meaning) {
		// generate message to server
		JSONObject message = new JSONObject();
		message.put("command", "update");
		message.put("word", word);
		message.put("meaning", meaning);

		// send the request to server and get the response
		String response = sendRequest(message.toString());
		JSONObject responseJson = new JSONObject(response);
		return responseJson.getString("status").equals("Successful");
	}

}
