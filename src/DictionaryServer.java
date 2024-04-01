import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class DictionaryServer {
	
	// Declare the port number
	private static int port;
	private static String dictionaryFile;

	
	// Identifies the user number connected
	private static int counter = 0;

	static String command;
	static String word;
	static String meaning;

	public static void main(String[] args)
	{
		// read port number and dictionary file from the command line
		try {
			port = Integer.parseInt(args[0]);
			dictionaryFile = args[1];
		}
		catch(Exception e) {
			//TODO: for testing, delete later
			port = 30005;
			dictionaryFile = "dictionary.JSON";
			//JOptionPane.showMessageDialog(null, "Please enter the 'Port number' and 'Dictionary file path'.", "Server Error", JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			// create a server socket listening on the given port
			ServerSocket server = new ServerSocket(port);
			System.out.println("Waiting for client connection-");
			
			// wait for connections
			while(true)
			{
				Socket client = server.accept();
				counter++;
				System.out.println("Client "+counter+": Applying for connection!");
							
				// start a new thread for a connection
				Thread t = new Thread(() -> serveClient(client));
				t.start();
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Please enter a valid port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void serveClient(Socket client)
	{
		try(Socket clientSocket = client)
		{
			// Input stream & Output Stream
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
		    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

			// receive message from the client
		    System.out.println("CLIENT: " + input.readUTF());
			String clientMessage = input.readUTF();
			JSONObject clientMessageJson = new JSONObject(clientMessage);
			command = clientMessageJson.getString("command");
			word = clientMessageJson.getString("word");

			String serverResponse = null;
			if (command.equals("search")) {
				serverResponse = searchWord(word, dictionaryFile);
			}
			else if (command.equals("add")) {
				meaning = clientMessageJson.getString("meaning");
				serverResponse = addWord(word, meaning, dictionaryFile);
			}
			else if (command.equals("remove")) {
				serverResponse = removeWord(word, dictionaryFile);
			}
			else if (command.equals("update")) {
				JSONArray meanings = clientMessageJson.getJSONArray("meaning");
				serverResponse = updateWord(word, meanings, dictionaryFile);
			}

			// send response back to the client
			output.writeUTF(serverResponse + "\n");
			output.flush();
			System.out.println("Response sent");

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Search the meaning of a word in the dictionary.
	 * @param word word to be searched
	 * @param dictionaryFile file path of the dictionary
	 * @return meaning of the word
	 */
	private static String searchWord(String word, String dictionaryFile) {
		JSONObject dict = new JSONObject(dictionaryFile);
		JSONObject response = new JSONObject();
		// check if the dictionary includes the word
		if (dict.has(word)) {
			String meaning = dict.optString(word);
			response.put("meaning", meaning);
		} else {
			response.put("meaning", "Word not found.");
		}
		return response.toString();
	}

	private static String addWord(String word, String meaning, String dictionaryFile) {
		JSONObject dict = new JSONObject(dictionaryFile);
		JSONObject response = new JSONObject();
		// check if the dictionary includes the word
		if (dict.has(word)) {
			response.put("status", "Duplicate");
			return response.toString();
		} else {
			// add the word and the meaning to the dictionary
			JSONObject newDict = dict.put(word, meaning);
			try (FileWriter file = new FileWriter(dictionaryFile);) {
				file.write(newDict.toString());
				System.out.println("Successfully added new word to dictionary!!");
				response.put("status", "Successful");
				return response.toString();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "Error";
	}

	private static String removeWord(String word, String dictionaryFile) {
		JSONObject dict = new JSONObject(dictionaryFile);
		JSONObject response = new JSONObject();
		// check if the dictionary includes the word
		if (dict.has(word)) {
			response.put("status", "Word not found.");
			return response.toString();
		} else {
			// remove word from dictionary
			dict.remove(word);
			try (FileWriter file = new FileWriter(dictionaryFile);) {
				file.write(dict.toString());
				System.out.println("Successfully removed the word from dictionary!!");
				response.put("status", "Successful");
				return response.toString();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "Error";
	}

	private static String updateWord(String word, JSONArray newMeanings, String dictionaryFile) {
		JSONObject dict = new JSONObject(dictionaryFile);
		JSONObject response = new JSONObject();
		// check if the dictionary includes the word
		if (dict.has(word)) {
			response.put("status", "Word not found.");
			return response.toString();
		} else {
			// add meanings of the word that are not in dictionary
			JSONArray dictMeanings = dict.optJSONArray(word);
			// for each input meaning, if it not exists in dictionary, add to dictionary
			for (int i = 0; i < newMeanings.length(); i++) {
				String meaning = newMeanings.getString(i);
				if (! dictMeanings.toString().contains("\"" + meaning + "\"")) {
					dictMeanings.put(meaning);
				}
			}

			// update the dictionary file
			dict.put(word, dictMeanings.toString());
			try (FileWriter file = new FileWriter(dictionaryFile);) {
				file.write(dict.toString());
				System.out.println("Successfully updated the meaning of the word in dictionary!!");
				response.put("status", "Successful");
				return response.toString();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "Error";
	}
}
