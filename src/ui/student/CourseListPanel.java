package ui.student;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Date;
import models.Course;
import models.Enrollment;
import service.CourseServiceClient;
import service.EnrollmentServiceClient;
import ui.auth.LoginScreen;

public class CourseListPanel extends JPanel {
    private int studentId;
    private CourseServiceClient courseServiceClient;
    private EnrollmentServiceClient enrollmentServiceClient;

    public CourseListPanel(int studentId) {
        this.studentId = studentId;
        this.courseServiceClient = new CourseServiceClient();
        this.enrollmentServiceClient = new EnrollmentServiceClient();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Course Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(63, 81, 181));
        titleLabel.setPreferredSize(new Dimension(getWidth(), 60));

        JPanel containerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        containerPanel.setBackground(new Color(245, 245, 245));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel enrolledPanel = new JPanel();
        enrolledPanel.setLayout(new BoxLayout(enrolledPanel, BoxLayout.Y_AXIS));
        enrolledPanel.setBackground(Color.WHITE);
        enrolledPanel.setBorder(BorderFactory.createTitledBorder("Enrolled Courses"));

        List<Course> enrolledCourses = courseServiceClient.getCoursesByStudentId(studentId);
        if (enrolledCourses != null && !enrolledCourses.isEmpty()) {
            for (Course course : enrolledCourses) {
                JPanel courseCard = createSimpleCourseCard(course);
                JButton deleteButton = new JButton("Unenroll");
                deleteButton.setBackground(new Color(255, 87, 34));
                deleteButton.setForeground(Color.WHITE);
                deleteButton.addActionListener(e -> {
                    int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to unenroll from " + course.getTitle() + "?", "Confirm Unenroll", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        List<Enrollment> enrollments = enrollmentServiceClient.getEnrollmentsByStudent(studentId);
                        for (Enrollment enrollment : enrollments) {
                            if (enrollment.getCourseId() == course.getId()) {
                                boolean success = enrollmentServiceClient.deleteEnrollment(enrollment.getId());
                                if (success) {
                                    JOptionPane.showMessageDialog(this, "Successfully unenrolled from " + course.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
                                    removeAll();
                                    revalidate();
                                    repaint();
                                    add(new CourseListPanel(studentId));
                                } else {
                                    JOptionPane.showMessageDialog(this, "Failed to unenroll.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                });

                courseCard.add(deleteButton);
                enrolledPanel.add(courseCard);
                enrolledPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            enrolledPanel.add(new JLabel("You are not enrolled in any courses."));
        }

        JScrollPane enrolledScrollPane = new JScrollPane(enrolledPanel);

        JPanel availablePanel = new JPanel();
        availablePanel.setLayout(new BoxLayout(availablePanel, BoxLayout.Y_AXIS));
        availablePanel.setBackground(Color.WHITE);
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available Courses"));

        List<Course> allCourses = courseServiceClient.getAllCourses();
        if (allCourses != null && !allCourses.isEmpty()) {
            for (Course course : allCourses) {
                JPanel courseCard = createCourseCard(course);
                availablePanel.add(courseCard);
                availablePanel.add(Box.createVerticalStrut(10));
            }
        } else {
            availablePanel.add(new JLabel("No available courses at the moment."));
        }

        JScrollPane availableScrollPane = new JScrollPane(availablePanel);

        containerPanel.add(enrolledScrollPane);
        containerPanel.add(availableScrollPane);

        add(titleLabel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);


        // 🏹 Back Button (now in the BorderLayout)
        ImageIcon backIcon = new ImageIcon(LoginScreen.class.getClassLoader().getResource("back_arrow.png"));
        Image img = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton backButton = new JButton(new ImageIcon(img));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        backButton.addActionListener(e -> {
            // Get the parent window (JFrame) that contains this panel
            Window parentWindow = SwingUtilities.getWindowAncestor(CourseListPanel.this);
            if (parentWindow != null) {
                parentWindow.dispose(); // Close the current window
            }

            StudentDashboard Screen = new StudentDashboard(studentId);
            Screen.setVisible(true);
        });

        // Add back button to the top left
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(240, 248, 255));
        backPanel.add(backButton);

        // Add backPanel and mainPanel to the frame
        add(backPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);
    }

    private JPanel createSimpleCourseCard(Course course) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(new Color(63, 81, 181)));
        card.setBackground(new Color(232, 234, 246));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel title = new JLabel(course.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(63, 81, 181));
        card.add(title);

        JLabel level = new JLabel("Level: " + course.getLevel());
        level.setFont(new Font("SansSerif", Font.PLAIN, 12));
        card.add(level);

        return card;
    }

    private JPanel createCourseCard(Course course) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 255));
        card.setBorder(BorderFactory.createLineBorder(new Color(63, 81, 181)));

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(new Color(63, 81, 181));

        JLabel descriptionLabel = new JLabel("<html><p style='width: 250px;'>" + course.getDescription() + "</p></html>");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descriptionLabel.setForeground(new Color(100, 100, 100));

        JButton enrollButton = new JButton("Enroll");
        enrollButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        enrollButton.setBackground(new Color(63, 81, 181));
        enrollButton.setForeground(Color.WHITE);
        enrollButton.setFocusPainted(false);
        enrollButton.addActionListener(e -> {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);
            enrollment.setCourseId(course.getId());
            enrollment.setDate(new Date());

            boolean success = enrollmentServiceClient.enrollStudent(enrollment);
            if (success) {
                JOptionPane.showMessageDialog(this, "Enrolled in " + course.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
                removeAll();
                revalidate();
                repaint();
                add(new CourseListPanel(studentId));
            } else {
                JOptionPane.showMessageDialog(this, "Enrollment failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descriptionLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(enrollButton);

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        return card;
    }
}
