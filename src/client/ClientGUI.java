package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ClientGUI extends JFrame{
    private JLabel word;
    private JLabel meaning;
    private JTextField wordInput;
    private JTextField meaningInput;
    private JButton searchButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton updateButton;
    private JTextArea output;
    private JPanel panelMain;
    private String PLACEHOLDER_TEXT = "Please separate multiple meanings by semicolon.";

    // store a client object
    private DictionaryClient client = new DictionaryClient();

    public ClientGUI() {
        setContentPane(panelMain);
        setTitle("Multi-threaded Dictionary Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        addPlaceholderStyle(meaningInput);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordInput.getText();
                // no input in word text field
                if (word.isEmpty()) {
                    output.setText("Please enter a word.");
                    return;
                }
                // search the word in dictionary and display in the text area below
                String meaning = client.search(word);
                output.setText(meaning);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordInput.getText();
                String meaning = meaningInput.getText();
                // no input in both word and meaning text fields
                if(word.isEmpty() && (meaning.isEmpty() || meaning.equals(PLACEHOLDER_TEXT))) {
                    output.setText("Please enter the word and its meaning.");
                    return;
                }
                // no word input
                else if(word.isEmpty()) {
                    output.setText("Please type a word.");
                    return;
                }
                // no meaning input
                else if(meaning.isEmpty() || meaning.equals(PLACEHOLDER_TEXT)) {
                    output.setText("Please type the meaning.");
                    return;
                }

                // add a new word and the meaning to the dictionary
                boolean status = client.add(word, meaning);
                if (status) {
                    output.setText("Added successfully!");
                } else {
                    output.setText("The word is already in the dictionary.");
                }

            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordInput.getText();
                // no input in word text field
                if (word.isEmpty()) {
                    output.setText("Please enter a word.");
                    return;
                }
                // remove the word and all its meanings in dictionary
                boolean status = client.remove(word);
                if (status) {
                    output.setText("Removed successfully!");
                } else {
                    output.setText("Word not found.");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordInput.getText();
                String meaning = meaningInput.getText();
                // no input in both word and meaning text fields
                if(word.isEmpty() && (meaning.isEmpty() || meaning.equals(PLACEHOLDER_TEXT))) {
                    output.setText("Please enter the word and its meaning.");
                    return;
                }
                // no word input
                else if(word.isEmpty()) {
                    output.setText("Please type a word.");
                    return;
                }
                // no meaning input
                else if(meaning.isEmpty() || meaning.equals(PLACEHOLDER_TEXT)) {
                    output.setText("Please type the meaning.");
                    return;
                }

                // update the word and its meanings in dictionary
                boolean status = client.update(word, meaning);
                if (status) {
                    output.setText("Updated successfully!");
                } else {
                    output.setText("Word not found.");
                }


            }
        });
        meaningInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (meaningInput.getText().equals(PLACEHOLDER_TEXT)) {
                    meaningInput.setText(null);
                    meaningInput.requestFocus();
                    removePlaceholderStyle(meaningInput);
                }
            }
        });
    }

    private void addPlaceholderStyle(JTextField textField) {
        textField.setForeground(Color.gray);
        Font font = textField.getFont();
        font = font.deriveFont(Font.ITALIC);
        textField.setFont(font);
    }

    private void removePlaceholderStyle(JTextField textField) {
        textField.setForeground(Color.black);
        Font font = textField.getFont();
        font = font.deriveFont(Font.PLAIN);
        textField.setFont(font);
    }

}
