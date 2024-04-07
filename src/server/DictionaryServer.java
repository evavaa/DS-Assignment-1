package server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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
			// launch the application
			ServerGUI serverGUI = new ServerGUI(dictionaryFile);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Please provide port number and path of the dictionary file!", "Server Error", JOptionPane.ERROR_MESSAGE);
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
				System.out.println("Request "+counter+": Applying for connection!");
							
				// start a new thread for a connection
				Thread t = new Thread(() -> serveClient(client));
				t.start();
			}
		} 
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Please enter a valid port number!", "Server Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
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
			String clientMessage = input.readUTF();
			System.out.println("CLIENT: " + clientMessage);
			JSONObject clientMessageJson = new JSONObject(clientMessage);
			String command = clientMessageJson.getString("command");
			String word = clientMessageJson.getString("word");

			String serverResponse = null;
			if (command.equals("search")) {
				serverResponse = searchWord(word);
			}
			else if (command.equals("add")) {
				String meaning = clientMessageJson.getString("meaning");
				serverResponse = addWord(word, meaning);
			}
			else if (command.equals("remove")) {
				serverResponse = removeWord(word);
			}
			else if (command.equals("update")) {
				String meaning = clientMessageJson.getString("meaning");
				serverResponse = updateWord(word, meaning);
			}

			// send response back to the client
			System.out.println(serverResponse);
			output.writeUTF(serverResponse + "\n");
			output.flush();
			System.out.println("Response sent");

		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Cannot connect to the client.", "Client Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Search the meaning of a word in the dictionary.
	 * @param word word to be searched
	 * @return meaning of the word
	 */
	private synchronized static String searchWord(String word) {
		try {
			// read the dictionary json file
			String content = new String(Files.readAllBytes(Paths.get(dictionaryFile)));
			JSONObject dict = new JSONObject(content);
			JSONObject response = new JSONObject();

			// check if the dictionary includes the word
			if (dict.has(word)) {
				String meaning = dict.optString(word);
				response.put("meaning", meaning);
			} else {
				response.put("meaning", "Word not found.");
			}
			return response.toString();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot load the dictionary.", "Server Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return "Error";
	}

	private synchronized static String addWord(String word, String meaning) {
		try {
			// read the dictionary json file
			String content = new String(Files.readAllBytes(Paths.get(dictionaryFile)));
			JSONObject dict = new JSONObject(content);
			JSONObject response = new JSONObject();

			// check if the dictionary includes the word
			if (dict.has(word)) {
				response.put("status", "Duplicate");
				return response.toString();
			} else {
				// add the word and the meaning to the dictionary
				List<String> meaningList = Arrays.asList(meaning.split("\\s*;\\s*"));
				JSONObject newDict = dict.put(word, meaningList);

				try (FileWriter file = new FileWriter(dictionaryFile)) {
					file.write(newDict.toString());
					System.out.println("Successfully added new word to dictionary!!");
					response.put("status", "Successful");
					return response.toString();

				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Cannot modify the dictionary.", "Server Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot load the dictionary.", "Server Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return "Error";
	}

	private synchronized static String removeWord(String word) {
		try {
			// read the dictionary json file
			String content = new String(Files.readAllBytes(Paths.get(dictionaryFile)));
			JSONObject dict = new JSONObject(content);
			JSONObject response = new JSONObject();

			// check if the dictionary includes the word
			if (! dict.has(word)) {
				response.put("status", "Word not found.");
				return response.toString();
			} else {
				// remove word from dictionary
				dict.remove(word);
				try (FileWriter file = new FileWriter(dictionaryFile)) {
					file.write(dict.toString());
					System.out.println("Successfully removed the word from dictionary!!");
					response.put("status", "Successful");
					return response.toString();

				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Cannot modify the dictionary.", "Server Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot load the dictionary.", "Server Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return "Error";
	}

	private synchronized static String updateWord(String word, String newMeanings) {
		try {
			// read the dictionary json file
			String content = new String(Files.readAllBytes(Paths.get(dictionaryFile)));
			JSONObject dict = new JSONObject(content);
			JSONObject response = new JSONObject();

			// check if the dictionary includes the word
			if (! dict.has(word)) {
				response.put("status", "Word not found.");
				return response.toString();
			} else {
				// add meanings of the word that are not in dictionary
				JSONArray dictMeanings = dict.optJSONArray(word);
				List<String> meaningList = Arrays.asList(newMeanings.split("\\s*;\\s*"));
				// for each input meaning, if it does not exist in dictionary, add to dictionary
				for (String meaning : meaningList) {
					if (! dictMeanings.toString().contains("\"" + meaning + "\"")) {
						dictMeanings.put(meaning);
					}
				}

				// update the dictionary file
				dict.put(word, dictMeanings);
				try (FileWriter file = new FileWriter(dictionaryFile)) {
					file.write(dict.toString());
					System.out.println("Successfully updated the meaning of the word in dictionary!!");
					response.put("status", "Successful");
					return response.toString();

				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Cannot modify the dictionary.", "Server Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot load the dictionary.", "Server Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return "Error";
	}
}
