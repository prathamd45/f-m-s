package sms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ConnectionView {

    static JFrame connectionFrame;

    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField databaseUrlField;

    public static void main(String[] args) {

        // Detect headless environment (Docker/K8s)
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            System.out.println("HEADLESS MODE - UI DISABLED");

            try {
                while (true) Thread.sleep(60000);
            } catch (InterruptedException e) { }
            return;
        }

        EventQueue.invokeLater(() -> {
            try {
                ConnectionView window = new ConnectionView();
                window.connectionFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.toString(), "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public ConnectionView() {
        initialize();
        connectionFrame.setVisible(true);
    }

    private void initialize() {
        connectionFrame = new JFrame();
        connectionFrame.setBounds(100, 100, 640, 480);
        connectionFrame.setResizable(false);
        connectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectionFrame.setTitle("Faculty Management System");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(SystemColor.textHighlight);
        connectionFrame.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel connectText = new JLabel("Connect to Database");
        connectText.setForeground(Color.WHITE);
        connectText.setFont(new Font("Tahoma", Font.BOLD, 24));
        topPanel.add(connectText);

        JPanel bottomPanel = new JPanel();
        connectionFrame.getContentPane().add(bottomPanel, BorderLayout.CENTER);
        bottomPanel.setLayout(null);

        JLabel databaseUrlText = new JLabel("Database URL:");
        databaseUrlText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        databaseUrlText.setBounds(70, 90, 162, 25);

        databaseUrlField = new JTextField();
        databaseUrlField.setName("databaseUrlField");
        databaseUrlField.setText("jdbc:mysql://localhost:3306/studentsdb");
        databaseUrlField.setBounds(240, 95, 330, 22);

        JLabel loginText = new JLabel("MySQL Username:");
        loginText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginText.setBounds(70, 135, 162, 25);

        loginField = new JTextField();
        loginField.setName("loginField");
        loginField.setBounds(240, 140, 330, 22);

        JLabel passwordText = new JLabel("MySQL Password:");
        passwordText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordText.setBounds(70, 175, 162, 25);

        passwordField = new JPasswordField();
        passwordField.setName("passwordField");
        passwordField.setBounds(240, 180, 330, 22);

        JButton connectButton = new JButton("Connect");
        connectButton.setBounds(230, 290, 180, 40);
        connectButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        bottomPanel.add(connectButton);

        bottomPanel.add(databaseUrlText);
        bottomPanel.add(databaseUrlField);
        bottomPanel.add(loginText);
        bottomPanel.add(loginField);
        bottomPanel.add(passwordText);
        bottomPanel.add(passwordField);

        connectButton.addActionListener(e -> connectToDB());
    }

    private void connectToDB() {
        if (loginField.getText().isEmpty() || databaseUrlField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(connectionFrame, "Please fill all required fields.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DBHandler.setLogin(loginField.getText());
        DBHandler.setPassword(String.valueOf(passwordField.getPassword()));
        DBHandler.setDatabaseUrl(databaseUrlField.getText());

        if (DBHandler.createTables()) {
            JOptionPane.showMessageDialog(connectionFrame, "Connected Successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            ManagementView.main(null);
            connectionFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(connectionFrame, "Connection Failed!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
