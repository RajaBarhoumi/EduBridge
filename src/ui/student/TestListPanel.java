package ui.student;

import models.Question;
import models.StudentTest;
import models.Test;
import service.QuestionServiceClient;
import service.StudentTestServiceClient;
import service.TestServiceClient;
import ui.auth.LoginScreen;
import ui.onboarding.OnboardingSlider;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.List;

public class TestListPanel extends JPanel {
    private int studentId;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private final Color SECONDARY_COLOR = new Color(255, 255, 255);
    private final Color ACCENT_COLOR = new Color(255, 152, 0);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private final Color CARD_BACKGROUND = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(224, 224, 224);

    public TestListPanel(int studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        TestServiceClient testServiceClient = new TestServiceClient();
        List<Test> testList = testServiceClient.getTestsByStudentId(studentId);

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Available Tests");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);

        if (testList.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tests available at the moment.");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(117, 117, 117));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(emptyLabel);
        } else {
            for (Test test : testList) {
                contentPanel.add(createTestCard(test));
                contentPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);


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
            Window parentWindow = SwingUtilities.getWindowAncestor(TestListPanel.this);
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
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTestCard(Test test) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Test info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(CARD_BACKGROUND);
        infoPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        JLabel testNameLabel = new JLabel(test.getTitle());
        testNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        testNameLabel.setForeground(PRIMARY_COLOR);

        TestServiceClient serviceClient = new TestServiceClient();
        int questionCount = serviceClient.getNumberOfQuestions(test.getId());
        int timeLimit = test.getTimeLimit();

        JLabel detailsLabel = new JLabel(
                String.format("%d questions • %d minutes", questionCount, timeLimit)
        );
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsLabel.setForeground(new Color(117, 117, 117));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CARD_BACKGROUND);
        textPanel.add(testNameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(detailsLabel);

        infoPanel.add(textPanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.CENTER);

        // Start button
        JButton startButton = createStyledButton("Start Test", SUCCESS_COLOR);
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        startButton.setPreferredSize(new Dimension(120, 40));
        startButton.addActionListener(e -> showTestConfirmation(test));

        card.add(startButton, BorderLayout.EAST);

        return card;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void showTestConfirmation(Test test) {
        TestServiceClient serviceClient = new TestServiceClient();
        int questionCount = serviceClient.getNumberOfQuestions(test.getId());
        int timeLimit = test.getTimeLimit();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(test.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JTextArea detailsArea = new JTextArea(
                String.format("Questions: %d\nTime Limit: %d minutes\n\n" +
                                "To receive a certificate, you must answer at least 80%% of the questions correctly.",
                        questionCount, timeLimit)
        );
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsArea.setEditable(false);
        detailsArea.setBackground(panel.getBackground());
        detailsArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(detailsArea, BorderLayout.CENTER);

        int response = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Test Information",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            startTest(test, studentId);
        }
    }

    private void startTest(Test test, int studentId) {
        StudentTestServiceClient studentTestService = new StudentTestServiceClient();
        StudentTest studentTest = new StudentTest();

        studentTest.setStudentId(studentId);
        studentTest.setTestId(test.getId());
        studentTest.setScore(0f);
        studentTest.setPassed(false);
        studentTest.setTakenDate(new Timestamp(System.currentTimeMillis()));

        int studentTestId = studentTestService.addStudentTest(studentTest);

        if (studentTestId == -1) {
            JOptionPane.showMessageDialog(this, "Failed to register test attempt. Please try again.");
            return;
        }

        QuestionServiceClient questionService = new QuestionServiceClient();
        List<Question> questions = questionService.getQuestionsByTest(test.getId());

        if (questions != null && !questions.isEmpty()) {
            // Create the TestScreen frame
            TestScreen testScreen = new TestScreen(test, studentTestId, questions);

            // Change the default close operation
            testScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Center the window relative to the parent
            testScreen.setLocationRelativeTo(this);
            testScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No questions found for this test.");
        }
    }
}