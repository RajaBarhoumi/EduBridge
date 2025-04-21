package ui.student;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.Course;
import models.Enrollment;
import service.CourseServiceClient;
import service.EnrollmentServiceClient;

import java.util.Date;

public class CourseListPanel extends JPanel {
    private int studentId;

    private CourseServiceClient courseServiceClient;
    private EnrollmentServiceClient enrollmentServiceClient;

    public CourseListPanel(int studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // Light background color for the panel

        // Initialize the service clients
        courseServiceClient = new CourseServiceClient();
        enrollmentServiceClient = new EnrollmentServiceClient();

        // Fetch all courses from the server
        List<Course> courses = courseServiceClient.getAllCourses();

        // Header with Title
        JLabel titleLabel = new JLabel("Available Courses", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(63, 81, 181)); // Gradient color
        titleLabel.setPreferredSize(new Dimension(getWidth(), 60));

        // Create a panel for course cards
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new GridLayout(0, 2, 20, 20)); // 2 columns with gap between cards
        coursePanel.setBackground(new Color(245, 245, 245));

        // Check if courses are available and create cards for each course
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
                JPanel courseCard = createCourseCard(course);
                coursePanel.add(courseCard);
            }
        } else {
            JLabel noCoursesLabel = new JLabel("No courses available", SwingConstants.CENTER);
            noCoursesLabel.setFont(new Font("SansSerif", Font.ITALIC, 20));
            noCoursesLabel.setForeground(new Color(158, 158, 158));
            coursePanel.add(noCoursesLabel);
        }

        // Scrollable area for the course panel
        JScrollPane scrollPane = new JScrollPane(coursePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the main panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Helper method to create a course card
    private JPanel createCourseCard(Course course) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 200));
        card.setBackground(new Color(255, 255, 255)); // White card background
        card.setBorder(BorderFactory.createLineBorder(new Color(63, 81, 181), 2, true)); // Blue border

        // Title Label
        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(63, 81, 181)); // Blue color for the title

        // Description Label
        JLabel descriptionLabel = new JLabel("<html><p style='width: 250px;'>" + course.getDescription() + "</p></html>");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(158, 158, 158)); // Light grey for description

        // Enroll Button
        JButton enrollButton = new JButton("Enroll");
        enrollButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        enrollButton.setBackground(new Color(63, 81, 181)); // Blue background for the button
        enrollButton.setForeground(Color.WHITE); // White text
        enrollButton.setFocusPainted(false);
        enrollButton.setBorderPainted(false);
        enrollButton.addActionListener(e -> {
            // Handle enroll action (you can add the enrollment logic here)
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);  // Assuming student ID is 1 for the example
            enrollment.setCourseId(course.getId());
            enrollment.setDate(new Date());  // Set current date

            boolean success = enrollmentServiceClient.enrollStudent(enrollment);
            if (success) {
                JOptionPane.showMessageDialog(this, "Successfully enrolled in " + course.getTitle(), "Enrollment Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Enrollment failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to the card
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10)); // Add some space between title and description
        card.add(descriptionLabel);
        card.add(Box.createVerticalStrut(10)); // Space between description and button
        card.add(enrollButton);
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        return card;
    }
}
