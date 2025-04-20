package ui.professor;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;
import models.Course;
import service.CourseServiceClient;

public class CourseListScreen extends JFrame {

    private int professorId;

    public CourseListScreen(int professorId) {
        this.professorId = professorId;
        setTitle("Your Courses");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        // Gradient Background Panel
        JPanel topPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(63, 81, 181),
                        getWidth(), getHeight(), new Color(103, 58, 183));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setPreferredSize(new Dimension(getWidth(), 150));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("List of Your Courses");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Course List Panel
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        coursePanel.setBackground(Color.WHITE);

        // Get courses for the professor
        List<Course> courses = new CourseServiceClient().getCoursesByProfessorId(professorId);
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
                coursePanel.add(createCourseCard(course));
            }
        } else {
            coursePanel.add(new JLabel("You have no courses at the moment."));
        }

        JScrollPane scrollPane = new JScrollPane(coursePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Add topPanel and make the window visible
        add(topPanel, BorderLayout.NORTH);
        setVisible(true);
    }

    // Method to create a card for each course
    private JPanel createCourseCard(Course course) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(100, 181, 246));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(200, 120));

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel levelLabel = new JLabel("Level: " + course.getLevel().name());
        levelLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        levelLabel.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(100, 181, 246));

        JButton detailsButton = new JButton("Details");
        detailsButton.setBackground(new Color(63, 81, 181));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailsButton.setFocusPainted(false);
        detailsButton.setPreferredSize(new Dimension(120, 35));
        detailsButton.addActionListener(e -> {
            // Open Course Details or Actions
            new CourseDetailsScreen(course.getId()).setVisible(true);
        });

        bottomPanel.add(detailsButton);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(levelLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }
}
