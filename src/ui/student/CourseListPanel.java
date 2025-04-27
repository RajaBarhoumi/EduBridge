package ui.student;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Date;
import models.Course;
import models.Enrollment;
import service.CourseServiceClient;
import service.EnrollmentServiceClient;

public class CourseListPanel extends JFrame {
    private int studentId;
    private CourseServiceClient courseServiceClient;
    private EnrollmentServiceClient enrollmentServiceClient;
    private JPanel enrolledPanel;
    private JPanel availablePanel;

    public CourseListPanel(int studentId) {
        this.studentId = studentId;
        this.courseServiceClient = new CourseServiceClient();
        this.enrollmentServiceClient = new EnrollmentServiceClient();

        setTitle("Course List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // Title
        JLabel titleLabel = new JLabel("Course Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(63, 81, 181));
        titleLabel.setPreferredSize(new Dimension(getWidth(), 60));
        add(titleLabel, BorderLayout.NORTH);

        // Back Button
        ImageIcon backIcon = new ImageIcon(CourseListPanel.class.getClassLoader().getResource("back_arrow.png"));
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

        // Container
        JPanel containerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        containerPanel.setBackground(new Color(245, 245, 245));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(containerPanel, BorderLayout.CENTER);

        // Enrolled Courses Panel
        enrolledPanel = new JPanel();
        enrolledPanel.setLayout(new BoxLayout(enrolledPanel, BoxLayout.Y_AXIS));
        enrolledPanel.setBackground(Color.WHITE);
        enrolledPanel.setBorder(BorderFactory.createTitledBorder("Enrolled Courses"));
        JScrollPane enrolledScrollPane = new JScrollPane(enrolledPanel);

        // Available Courses Panel
        availablePanel = new JPanel();
        availablePanel.setLayout(new BoxLayout(availablePanel, BoxLayout.Y_AXIS));
        availablePanel.setBackground(Color.WHITE);
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available Courses"));
        JScrollPane availableScrollPane = new JScrollPane(availablePanel);

        containerPanel.add(enrolledScrollPane);
        containerPanel.add(availableScrollPane);

        loadCourses();
    }

    private void loadCourses() {
        enrolledPanel.removeAll();
        availablePanel.removeAll();

        List<Course> enrolledCourses = courseServiceClient.getCoursesByStudentId(studentId);
        List<Course> allCourses = courseServiceClient.getAllCourses();

        if (enrolledCourses != null && !enrolledCourses.isEmpty()) {
            for (Course course : enrolledCourses) {
                JPanel courseCard = createSimpleCourseCard(course);
                JButton unenrollButton = new JButton("Unenroll");
                unenrollButton.setBackground(new Color(255, 87, 34));
                unenrollButton.setForeground(Color.WHITE);
                unenrollButton.addActionListener(e -> unenrollFromCourse(course));
                courseCard.add(unenrollButton);
                enrolledPanel.add(courseCard);
                enrolledPanel.add(Box.createVerticalStrut(10));
            }
        } else {
            enrolledPanel.add(new JLabel("You are not enrolled in any courses."));
        }

        if (allCourses != null && !allCourses.isEmpty()) {
            for (Course course : allCourses) {
                boolean isAlreadyEnrolled = enrolledCourses.stream().anyMatch(c -> c.getId() == course.getId());
                if (!isAlreadyEnrolled) {
                    JPanel courseCard = createCourseCard(course);
                    availablePanel.add(courseCard);
                    availablePanel.add(Box.createVerticalStrut(10));
                }
            }
        } else {
            availablePanel.add(new JLabel("No available courses at the moment."));
        }

        revalidate();
        repaint();
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
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(63, 81, 181)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(new Color(63, 81, 181));

        JLabel descriptionLabel = new JLabel("<html><p style='width: 250px;'>" + course.getDescription() + "</p></html>");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descriptionLabel.setForeground(new Color(100, 100, 100));

        JButton enrollButton = new JButton("Enroll");
        enrollButton.setBackground(new Color(63, 81, 181));
        enrollButton.setForeground(Color.WHITE);
        enrollButton.setFocusPainted(false);
        enrollButton.addActionListener(e -> enrollInCourse(course));

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descriptionLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(enrollButton);

        return card;
    }

    private void enrollInCourse(Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(course.getId());
        enrollment.setDate(new Date());

        boolean success = enrollmentServiceClient.enrollStudent(enrollment);
        if (success) {
            JOptionPane.showMessageDialog(this, "Enrolled in " + course.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCourses();
        } else {
            JOptionPane.showMessageDialog(this, "Enrollment failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void unenrollFromCourse(Course course) {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to unenroll from " + course.getTitle() + "?",
                "Confirm Unenroll", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            List<Enrollment> enrollments = enrollmentServiceClient.getEnrollmentsByStudent(studentId);
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseId() == course.getId()) {
                    boolean success = enrollmentServiceClient.deleteEnrollment(enrollment.getId());
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Successfully unenrolled from " + course.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadCourses();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to unenroll.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        }
    }
}
