package ui.professor;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

import models.Course;
import service.CourseServiceClient;

public class CourseDetailsScreen extends JFrame {

    public CourseDetailsScreen(int courseId) {
        setTitle("Course Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Retrieve the course using CourseService
        Course course = null;
        course = new CourseServiceClient().getCourseById(courseId);

        if (course != null) {
            // Header with gradient background
            JPanel headerPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(0, 0, new Color(63, 81, 181),
                            getWidth(), getHeight(), new Color(103, 58, 183));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            headerPanel.setPreferredSize(new Dimension(getWidth(), 120));
            headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel(course.getTitle());
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel);

            // Course Details Panel
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setBackground(Color.WHITE);

            JLabel descriptionLabel = new JLabel("<html><b>Description:</b> " + course.getDescription() + "</html>");
            descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel levelLabel = new JLabel("<html><b>Level:</b> " + course.getLevel().name() + "</html>");
            levelLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            detailsPanel.add(descriptionLabel);
            detailsPanel.add(levelLabel);

            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            add(scrollPane, BorderLayout.CENTER);
            add(headerPanel, BorderLayout.NORTH);
        }

        setVisible(true);
    }
}
