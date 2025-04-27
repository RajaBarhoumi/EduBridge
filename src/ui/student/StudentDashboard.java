package ui.student;

import models.User;
import service.EnrollmentServiceClient;
import service.StudentTestServiceClient;
import service.UserServiceClient;
import ui.auth.LoginScreen;
import ui.onboarding.OnboardingSlider;

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

        // Top panel with gradient and content
        JPanel topPanel = new JPanel(new BorderLayout()) {
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

        // Back button
        ImageIcon backIcon = new ImageIcon(LoginScreen.class.getClassLoader().getResource("logout.png"));
        Image img = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton backButton = new JButton(new ImageIcon(img));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            LoginScreen screen = new LoginScreen();
            screen.setVisible(true);

        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + (student != null ? student.getName() : "Student"), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);

        // Add back button and welcome label to topPanel
        topPanel.add(backButtonPanel, BorderLayout.WEST);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        summaryPanel.add(createSummaryCard("📘 Courses", String.valueOf(courseCount), this::openCourses, "course"));
        summaryPanel.add(createSummaryCard("📝 Tests", String.valueOf(testCount), this::openTests, "test"));
        summaryPanel.add(createSummaryCard("🎓 Certificates", String.valueOf(certificateCount), null, "certificate"));

        // Chart Panel (empty for now)
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
        // Create the main card panel
        JPanel card = new JPanel(new BorderLayout());
        //lena badel bidha
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(320, 240)); // Even larger card size
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // Determine image resource
        String imageName;
        switch (labelType) {
            case "course": imageName = "courses.png"; break;
            case "test": imageName = "test.png"; break;
            case "certificate": imageName = "certif.png"; break;
            default: imageName = "courses.png";
        }

        // Load and scale image to fill card width
        ImageIcon originalIcon = new ImageIcon(OnboardingSlider.class.getClassLoader().getResource(imageName));
        Image originalImage = originalIcon.getImage();

        // Calculate dimensions to fill card width (with 20px padding)
        int cardContentWidth = 280;
        int cardContentHeight = 180;
        int imgWidth = originalIcon.getIconWidth();
        int imgHeight = originalIcon.getIconHeight();

        // Scale to fill width while maintaining aspect ratio
        double scaleFactor = (double) cardContentWidth / imgWidth;
        int scaledWidth = cardContentWidth;
        int scaledHeight = (int) (imgHeight * scaleFactor);

        // If scaled height exceeds our content area, scale down
        if (scaledHeight > cardContentHeight) {
            scaleFactor = (double) cardContentHeight / imgHeight;
            scaledHeight = cardContentHeight;
            scaledWidth = (int) (imgWidth * scaleFactor);
        }

        // Create the scaled image
        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Image container with proper centering
        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setOpaque(false);
        imageContainer.add(imageLabel);

        // Text panel
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        Color lawn = new Color(33, 33, 99);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(lawn);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36)); // Larger font
        valueLabel.setForeground(lawn);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Store references for updates
        switch (labelType) {
            case "course": courseCountLabel = valueLabel; break;
            case "test": testCountLabel = valueLabel; break;
            case "certificate": certificateCountLabel = valueLabel; break;
        }

        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.SOUTH);

        // Add components to card
        card.add(imageContainer, BorderLayout.CENTER);
        card.add(textPanel, BorderLayout.SOUTH);

        // Add click handler if provided
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
        dispose();
    }

    private void openTests() {
        TestListPanel testListPanel = new TestListPanel(studentId);
        JFrame testFrame = new JFrame("Test List");
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.setSize(1000, 700);
        testFrame.add(testListPanel);
        testFrame.setVisible(true);
        dispose();
    }
}
