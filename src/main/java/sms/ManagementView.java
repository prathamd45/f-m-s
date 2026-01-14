package sms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagementView {

    static JFrame managementFrame;
    static JTable table;
    static JTextField facultyNameField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ManagementView window = new ManagementView();
                window.managementFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManagementView() {
        initialize();
        updateFacultyTable();
        managementFrame.setVisible(true);
    }

    private void updateFacultyTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String[] faculties = DBHandler.getFaculties();
        for (String f : faculties) {
            model.addRow(new Object[]{f});
        }
    }

    private void initialize() {
        managementFrame = new JFrame();
        managementFrame.setBounds(100, 100, 620, 400);
        managementFrame.setResizable(false);
        managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managementFrame.setTitle("Faculty Management System");
        managementFrame.getContentPane().setLayout(null);

        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(new LineBorder(SystemColor.textHighlight, 4));
        tablePanel.setBounds(260, 10, 345, 340);
        tablePanel.setLayout(null);
        managementFrame.getContentPane().add(tablePanel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 325, 320);
        tablePanel.add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Faculty Name"}
        ));
        scrollPane.setViewportView(table);

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new LineBorder(SystemColor.textHighlight, 4));
        leftPanel.setBounds(10, 10, 240, 340);
        leftPanel.setLayout(null);
        managementFrame.getContentPane().add(leftPanel);

        JLabel facultyLabel = new JLabel("Faculty Name:");
        facultyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        facultyLabel.setBounds(20, 20, 180, 25);
        leftPanel.add(facultyLabel);

        facultyNameField = new JTextField();
        facultyNameField.setBounds(20, 50, 180, 25);
        leftPanel.add(facultyNameField);

        JButton addButton = new JButton("Add Faculty");
        addButton.setBounds(20, 90, 180, 35);
        addButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        leftPanel.add(addButton);

        addButton.addActionListener(e -> addFaculty());

        JButton deleteButton = new JButton("Delete Faculty");
        deleteButton.setBounds(20, 140, 180, 35);
        deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        leftPanel.add(deleteButton);

        deleteButton.addActionListener(e -> deleteFaculty());

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setBounds(20, 190, 180, 35);
        leftPanel.add(disconnectButton);

        disconnectButton.addActionListener(e -> {
            ConnectionView.main(null);
            managementFrame.dispose();
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(20, 240, 180, 35);
        leftPanel.add(exitButton);

        exitButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(managementFrame,
                    "Exit the application?", "Exit",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void addFaculty() {
        String name = facultyNameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(managementFrame,
                    "Enter a faculty name.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DBHandler.checkIfElementExists(DBHandler.getFacultiesTable(), name)) {
            JOptionPane.showMessageDialog(managementFrame,
                    "Faculty already exists.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DBHandler.addFaculty(name)) {
            JOptionPane.showMessageDialog(managementFrame,
                    "Faculty added!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            facultyNameField.setText("");
            updateFacultyTable();
        } else {
            JOptionPane.showMessageDialog(managementFrame,
                    "Error adding faculty.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFaculty() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(managementFrame,
                    "Select a faculty to delete.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String facultyName = table.getValueAt(row, 0).toString();

        if (JOptionPane.showConfirmDialog(managementFrame,
                "Delete '" + facultyName + "'?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            if (DBHandler.deleteFaculty(facultyName)) {
                JOptionPane.showMessageDialog(managementFrame,
                        "Deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                updateFacultyTable();
            } else {
                JOptionPane.showMessageDialog(managementFrame,
                        "Error deleting faculty.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
