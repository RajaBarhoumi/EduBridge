package ui.student;

import models.Test;
import models.User;
import service.EnrollmentServiceClient;
import service.StudentTestServiceClient;
import service.TestServiceClient;
import service.UserServiceClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentDashboard extends JFrame {

    private int studentId;
    private UserServiceClient client;
    private EnrollmentServiceClient enrollmentClient;
    private StudentTestServiceClient testClient;

    public StudentDashboard(int studentId) {
        this.enrollmentClient = new EnrollmentServiceClient();
        this.testClient = new StudentTestServiceClient();
        this.studentId = studentId;
        setTitle("Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        client = new UserServiceClient();
        User student = client.getUserById(studentId);

        int courseCount = enrollmentClient.getCourseCountByStudentId(studentId);
        int testCount = testClient.getTestCountByStudentId(studentId);
        int certificateCount = testClient.getCertificateCountByStudentId(studentId);

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
        JLabel welcomeLabel = new JLabel("Welcome, " + (student != null ? student.getName() : "Student"), SwingConstants.CENTER);

        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        summaryPanel.add(createSummaryCard("📘 Courses", String.valueOf(courseCount), () -> {
            openCourses();
        }));
        summaryPanel.add(createSummaryCard("📝 Tests", String.valueOf(testCount), () -> {
            openTests();
        }));
        summaryPanel.add(createSummaryCard("🎓 Certificates", String.valueOf(certificateCount), null));

        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(topPanel, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.CENTER);
        add(chartPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createSummaryCard(String title, String value, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(100, 181, 246));
        card.setPreferredSize(new Dimension(200, 100));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        if (onClick != null) {
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                }
            });
        }

        return card;
    }

    private void openCourses() {
        CourseListPanel courseListPanel = new CourseListPanel(studentId);
        JFrame courseFrame = new JFrame("Courses");
        courseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        courseFrame.setSize(1000, 700);
        courseFrame.add(courseListPanel);
        courseFrame.setVisible(true);
    }

    private void openTests() {
        TestListPanel testListPanel = new TestListPanel(studentId);
        JFrame testFrame = new JFrame("Test List");
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.setSize(1000, 700);
        testFrame.add(testListPanel);
        testFrame.setVisible(true);
    }
}
