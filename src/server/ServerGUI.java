package server;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class ServerGUI extends JFrame {
    private JPanel panelMain;
    private JTextArea dictionaryDisplay;
    private JButton refreshButton;

    public ServerGUI(String dictionaryFile) {
        setContentPane(panelMain);
        setTitle("Multi-threaded Dictionary Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // display information from dictionary file
        loadDictionary(dictionaryFile);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // display information from dictionary file
                loadDictionary(dictionaryFile);
            }
        });
    }

    private void loadDictionary(String dictionaryFile) {
        // display information from dictionary file
        try {
            String content = new String(Files.readAllBytes(Paths.get(dictionaryFile)));
            JSONObject dict = new JSONObject(content);
            Iterator<String> keys = dict.keys();

            String output = "";

            while(keys.hasNext()) {
                String word = keys.next();
                String dictMeanings = dict.optString(word);
                output = output + word + ": " + dictMeanings + "\n";
            }

            dictionaryDisplay.setText(output);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot find the dictionary file.", "Server Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

    }
}
