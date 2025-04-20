package ui.professor;

import models.Course;
import service.CourseServiceClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

public class AddCourseForm extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Course.Level> levelCombo;

    private int professorId;
    private CourseServiceClient courseService;

    public AddCourseForm(int professorId) {
        this.professorId = professorId;
        courseService = new CourseServiceClient(); // Initialize the service

        setTitle("➕ Add New Course");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Gradient Title Panel
        JPanel titlePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(63, 81, 181),
                        getWidth(), getHeight(),
                        new Color(103, 58, 183));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setPreferredSize(new Dimension(getWidth(), 80));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Add a New Course");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Course Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField();
        formPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(4, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Level
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 1;
        levelCombo = new JComboBox<>(Course.Level.values());
        formPanel.add(levelCombo, gbc);

        // Submit Button
        JButton saveBtn = new JButton("Save Course");
        styleButton(saveBtn);
        saveBtn.addActionListener(this::addCourseToDatabase);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);

        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(63, 81, 181));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
    }

    private void addCourseToDatabase(ActionEvent e) {
        // Create course object and set its attributes
        Course course = new Course();
        course.setTitle(titleField.getText());
        course.setDescription(descriptionArea.getText());
        course.setLevel((Course.Level) levelCombo.getSelectedItem());
        course.setProfessorId(professorId); // Important: Set the professor ID

        // Call CourseService to handle the RMI logic
        courseService.createCourse(course);

        JOptionPane.showMessageDialog(this, "Course added successfully.");
        dispose();
    }
}
