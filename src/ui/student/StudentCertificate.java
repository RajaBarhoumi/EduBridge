package ui.student;

import service.StudentTestServiceClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import ui.auth.LoginScreen;
import ui.student.StudentDashboard;

public class StudentCertificate extends JFrame {
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private StudentTestServiceClient studentTestServiceClient;

    public StudentCertificate(int studentId) {
        setTitle("Student Certificates");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentTestServiceClient = new StudentTestServiceClient();
        initComponents(studentId);
    }

    private void initComponents(int studentId) {
        tableModel = new DefaultTableModel(new Object[]{"Test Title", "Score"}, 0);
        resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);

        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        ImageIcon backIcon = new ImageIcon(StudentCertificate.class.getClassLoader().getResource("back_arrow.png"));
        Image img = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton backButton = new JButton(new ImageIcon(img));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            StudentDashboard dashboard = new StudentDashboard(studentId);
            dashboard.setVisible(true);
        });

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(240, 248, 255));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.NORTH);

        loadStudentTestResults(studentId);
    }

    private void loadStudentTestResults(int studentId) {
        List<Map<String, Object>> results = studentTestServiceClient.getStudentTestResults(studentId);

        if (results != null) {
            for (Map<String, Object> result : results) {
                String title = (String) result.get("title");
                Float score = (Float) result.get("score");
                tableModel.addRow(new Object[]{title, score});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load student test results.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
