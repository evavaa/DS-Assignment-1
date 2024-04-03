import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ServerGUI extends JFrame {
    private JPanel panelMain;
    private JTextArea dictionaryDisplay;
    private JButton refreshButton;
    private String dictionaryFile;

    public ServerGUI(String dictionaryFile) {
        this.dictionaryFile = dictionaryFile;
        setContentPane(panelMain);
        setTitle("Multi-threaded Dictionary Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);

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
            ex.printStackTrace();
        }

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // load information from dictionary file
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
                    ex.printStackTrace();
                }
            }
        });
    }
}
