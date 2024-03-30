package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame{
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

    public MainWindow() {
        setContentPane(panelMain);
        setTitle("Multi-threaded Dictionary Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = wordInput.getText();
                output.append(input);
            }
        });
    }

    public static void main(String[] args) {
        new MainWindow();

    }

}
