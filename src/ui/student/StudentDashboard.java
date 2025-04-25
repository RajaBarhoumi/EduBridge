package ui.student;

import models.User;
import service.EnrollmentServiceClient;
import service.StudentTestServiceClient;
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
    private StudentDashboardHandler statsUpdaterThread;
    private JLabel courseCountLabel;
    private JLabel testCountLabel;
    private JLabel certificateCountLabel;

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

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        summaryPanel.add(createSummaryCard("📘 Courses", String.valueOf(courseCount), () -> {
            openCourses();
        }, "course"));
        summaryPanel.add(createSummaryCard("📝 Tests", String.valueOf(testCount), () -> {
            openTests();
        }, "test"));
        summaryPanel.add(createSummaryCard("🎓 Certificates", String.valueOf(certificateCount), null, "certificate"));

        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(topPanel, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.CENTER);
        add(chartPanel, BorderLayout.SOUTH);

        // Start the stats updater thread
        statsUpdaterThread = new StudentDashboardHandler(this, enrollmentClient, testClient);
        statsUpdaterThread.start();

        setVisible(true);
    }

    public int getStudentId() {
        return studentId;
    }

    // Update methods for dynamic statistics
    public void updateCourseCount(int newCount) {
        if (courseCountLabel != null) {
            courseCountLabel.setText(String.valueOf(newCount));
        }
    }

    public void updateTestCount(int newCount) {
        if (testCountLabel != null) {
            testCountLabel.setText(String.valueOf(newCount));
        }
    }

    public void updateCertificateCount(int newCount) {
        if (certificateCountLabel != null) {
            certificateCountLabel.setText(String.valueOf(newCount));
        }
    }

    @Override
    public void dispose() {
        if (statsUpdaterThread != null) {
            statsUpdaterThread.stopThread();
        }
        super.dispose();
    }

    private JPanel createSummaryCard(String title, String value, Runnable onClick, String labelType) {
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

        if ("course".equals(labelType)) {
            courseCountLabel = valueLabel;
        } else if ("test".equals(labelType)) {
            testCountLabel = valueLabel;
        } else if ("certificate".equals(labelType)) {
            certificateCountLabel = valueLabel;
        }

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