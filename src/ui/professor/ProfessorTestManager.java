package ui.professor;

import models.*;
import service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class ProfessorTestManager extends JFrame {
    private int professorId;
    private CourseServiceClient courseService = new CourseServiceClient();
    private TestServiceClient testService = new TestServiceClient();
    private QuestionServiceClient questionService = new QuestionServiceClient();
    private OptionServiceClient optionService = new OptionServiceClient();

    private JComboBox<Course> courseComboBox;
    private JPanel testContainer;
    private JScrollPane testScrollPane;

    // Colors
    private final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private final Color SECONDARY_COLOR = new Color(255, 255, 255);
    private final Color ACCENT_COLOR = new Color(255, 152, 0);
    private final Color DANGER_COLOR = new Color(244, 67, 54);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private final Color CARD_BACKGROUND = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(224, 224, 224);

    public ProfessorTestManager(int professorId) {
        this.professorId = professorId;

        setTitle("Test Manager");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        initComponents();
    }

    private void initComponents() {
        // Create a main panel to hold everything
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create back button
        ImageIcon backIcon = new ImageIcon(getClass().getClassLoader().getResource("back_arrow.png"));
        Image img = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton backButton = new JButton(new ImageIcon(img));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> {
            dispose(); // Close the current window
            new ProfessorDashboard(professorId).setVisible(true);
        });

        // Create back panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(BACKGROUND_COLOR);
        backPanel.add(backButton);

        // Top Panel (existing code)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        topPanel.setBackground(SECONDARY_COLOR);
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Test Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        topPanel.add(titleLabel);

        topPanel.add(Box.createHorizontalStrut(30));

        courseComboBox = new JComboBox<>();
        courseComboBox.setPreferredSize(new Dimension(250, 35));
        courseComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseComboBox.addActionListener(e -> loadTests());

        JButton addTestBtn = createStyledButton("+ Add Test", PRIMARY_COLOR);
        addTestBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addTestBtn.addActionListener(e -> openTestDialog(false,null));

        topPanel.add(new JLabel("Select Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(addTestBtn);

        // Test container (existing code)
        testContainer = new JPanel();
        testContainer.setLayout(new BoxLayout(testContainer, BoxLayout.Y_AXIS));
        testContainer.setBackground(BACKGROUND_COLOR);
        testContainer.setBorder(new EmptyBorder(10, 20, 20, 20));

        testScrollPane = new JScrollPane(testContainer);
        testScrollPane.setBorder(null);
        testScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Header panel to combine back button and title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(backPanel, BorderLayout.WEST);
        headerPanel.add(topPanel, BorderLayout.CENTER);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(testScrollPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        loadCourses();
    }
    private void loadCourses() {
        courseComboBox.removeAllItems();
        List<Course> courses = courseService.getAllCourses();
        for (Course course : courses) {
            courseComboBox.addItem(course);
        }
    }

    private void openTestDialog(boolean isUpdate, Test test) {
        JTextField titleField = new JTextField(test != null ? test.getTitle() : "", 20);
        JTextField timeLimitField = new JTextField(test != null ? String.valueOf(test.getTimeLimit()) : "", 5);
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Test Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Time Limit (mins):"));
        panel.add(timeLimitField);
//        panel.add(new JLabel("Max Attempts:"));

        int result = JOptionPane.showConfirmDialog(this, panel, isUpdate ? "Update Test" : "Add Test", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course.");
                return;
            }

            Test newTest = new Test();
            newTest.setCourseId(selectedCourse.getId());
            newTest.setTitle(titleField.getText());
            newTest.setProfessorId(professorId);
            try {
                newTest.setTimeLimit(Integer.parseInt(timeLimitField.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
                return;
            }

            if (isUpdate && test != null) {
                newTest.setId(test.getId());
                testService.updateTest(newTest);
            } else {
                testService.createTest(newTest);
            }
            loadTests();
        }
    }

    private void loadTests() {
        testContainer.removeAll();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        if (selectedCourse == null) return;

        List<Test> tests = testService.getTestsByCourseId(selectedCourse.getId());
        if (tests.isEmpty()) {
            JLabel noTestsLabel = new JLabel("No tests available for this course. Click 'Add Test' to create one.");
            noTestsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noTestsLabel.setForeground(new Color(117, 117, 117));
            noTestsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            testContainer.add(noTestsLabel);
        } else {
            for (Test test : tests) {
                testContainer.add(createTestCard(test));
                testContainer.add(Box.createVerticalStrut(15));
            }
        }

        testContainer.revalidate();
        testContainer.repaint();
    }

    private JPanel createTestCard(Test test) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Card Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BACKGROUND);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel(test.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(CARD_BACKGROUND);
        JButton updateBtn = createStyledButton("Update", ACCENT_COLOR);
        JButton deleteBtn = createStyledButton("Delete", DANGER_COLOR);
        JButton addQuestionBtn = createStyledButton("+ Add Question", SUCCESS_COLOR);

        updateBtn.addActionListener(e -> openTestDialog(true, test));
        deleteBtn.addActionListener(e -> deleteTest(test.getId()));
        addQuestionBtn.addActionListener(e -> openQuestionDialog(test.getId()));

        buttonPanel.add(updateBtn);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(deleteBtn);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(addQuestionBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        JPanel questionsContent = new JPanel();
        questionsContent.setLayout(new BoxLayout(questionsContent, BoxLayout.Y_AXIS));
        questionsContent.setBackground(CARD_BACKGROUND);

        List<Question> questions = questionService.getQuestionsByTest(test.getId());
        for (Question question : questions) {
            questionsContent.add(createQuestionPanel(test.getId(), question));
            questionsContent.add(Box.createVerticalStrut(15));
        }

        // Make questions scrollable
        JScrollPane questionsScrollPane = new JScrollPane(questionsContent);
        questionsScrollPane.setBorder(null);
        questionsScrollPane.setPreferredSize(new Dimension(0, Math.min(questions.size() * 100, 300)));
        questionsScrollPane.setVisible(false);

        JButton toggleBtn = createStyledButton("▼ Show Questions", new Color(96, 125, 139));
        toggleBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toggleBtn.addActionListener(e -> {
            questionsScrollPane.setVisible(!questionsScrollPane.isVisible());
            toggleBtn.setText(questionsScrollPane.isVisible() ? "▲ Hide Questions" : "▼ Show Questions");
            card.revalidate();
        });

        card.add(header, BorderLayout.NORTH);
        card.add(questionsScrollPane, BorderLayout.CENTER);
        card.add(toggleBtn, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createQuestionPanel(int testId, Question question) {
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(new Color(249, 249, 249));
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel questionLabel = new JLabel("<html>" + question.getContent() + " <font color='#616161'>(Points: " + question.getPoints() + ")</font></html>");
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton deleteQuestionBtn = createStyledButton("Delete", DANGER_COLOR);
        deleteQuestionBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deleteQuestionBtn.addActionListener(e -> deleteQuestion(testId, question.getId()));

        JPanel questionHeader = new JPanel(new BorderLayout());
        questionHeader.setBackground(new Color(249, 249, 249));
        questionHeader.add(questionLabel, BorderLayout.WEST);
        questionHeader.add(deleteQuestionBtn, BorderLayout.EAST);

        JPanel optionsContent = new JPanel();
        optionsContent.setLayout(new BoxLayout(optionsContent, BoxLayout.Y_AXIS));
        optionsContent.setBackground(new Color(249, 249, 249));

        List<Option> options = optionService.getOptionsByQuestion(question.getId());
        if (options != null && !options.isEmpty()) {
            for (Option option : options) {
                optionsContent.add(createOptionPanel(question.getId(), option));
                optionsContent.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noOptionsLabel = new JLabel("No options available");
            noOptionsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noOptionsLabel.setForeground(new Color(117, 117, 117));
            optionsContent.add(noOptionsLabel);
        }

        JScrollPane optionsScrollPane = new JScrollPane(optionsContent);
        optionsScrollPane.setBorder(null);
        optionsScrollPane.setPreferredSize(new Dimension(0, Math.min((options != null ? options.size() : 0) * 40, 150)));

        JButton addOptionBtn = createStyledButton("+ Add Option", SUCCESS_COLOR);
        addOptionBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addOptionBtn.addActionListener(e -> openOptionDialog(question.getId()));

        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(new Color(249, 249, 249));
        optionsPanel.add(optionsScrollPane, BorderLayout.CENTER);
        optionsPanel.add(addOptionBtn, BorderLayout.SOUTH);
        optionsPanel.setBorder(new EmptyBorder(10, 20, 0, 0));

        questionPanel.add(questionHeader, BorderLayout.NORTH);
        questionPanel.add(optionsPanel, BorderLayout.CENTER);

        return questionPanel;
    }

    private JPanel createOptionPanel(int questionId, Option option) {
        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.setBackground(Color.WHITE);
        optionPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JLabel optionLabel = new JLabel("<html>" + option.getContent() +
                (option.isCorrect() ? " <font color='#388E3C'>(Correct)</font>" : "") + "</html>");
        optionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton deleteOptionBtn = createStyledButton("Delete", DANGER_COLOR);
        deleteOptionBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        deleteOptionBtn.addActionListener(e -> deleteOption(questionId, option.getId()));

        optionPanel.add(optionLabel, BorderLayout.WEST);
        optionPanel.add(deleteOptionBtn, BorderLayout.EAST);

        return optionPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
        return button;
    }

//    private void openQuestionDialog(int testId) {
//        JDialog dialog = new JDialog(this, "Add Question", true);
//        dialog.setSize(450, 200);
//        dialog.setLocationRelativeTo(this);
//        dialog.setLayout(new BorderLayout());
//
//        JPanel panel = new JPanel(new GridBagLayout());
//        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
//        panel.setBackground(SECONDARY_COLOR);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 15, 5);
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        JLabel contentLabel = new JLabel("Question:");
//        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        JTextField contentField = new JTextField(20);
//        contentField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//
//        JLabel pointsLabel = new JLabel("Points:");
//        pointsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        JTextField pointsField = new JTextField(5);
//        pointsField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        panel.add(contentLabel, gbc);
//
//        gbc.gridy = 1;
//        gbc.gridwidth = 2;
//        panel.add(contentField, gbc);
//
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        panel.add(pointsLabel, gbc);
//
//        gbc.gridx = 1;
//        panel.add(pointsField, gbc);
//
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttonPanel.setBackground(SECONDARY_COLOR);
//
//        JButton cancelBtn = createStyledButton("Cancel", new Color(158, 158, 158));
//        cancelBtn.addActionListener(e -> dialog.dispose());
//
//        JButton saveBtn = createStyledButton("Save", PRIMARY_COLOR);
//        saveBtn.addActionListener(e -> {
//            String content = contentField.getText().trim();
//            String pointsText = pointsField.getText().trim();
//
//            if (content.isEmpty() || pointsText.isEmpty()) {
//                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            try {
//                int points = Integer.parseInt(pointsText);
//
//                Question question = new Question();
//                question.setContent(content);
//                question.setPoints(points);
//                question.setTestId(testId);
//
//                boolean success = questionService.addQuestion(question);
//                if (success) {
//                    JOptionPane.showMessageDialog(dialog, "Question added successfully!");
//                    loadTests();
//                    dialog.dispose();
//                } else {
//                    JOptionPane.showMessageDialog(dialog, "Failed to add question.", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(dialog, "Points must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//
//        buttonPanel.add(cancelBtn);
//        buttonPanel.add(saveBtn);
//
//        dialog.add(panel, BorderLayout.CENTER);
//        dialog.add(buttonPanel, BorderLayout.SOUTH);
//        dialog.setVisible(true);
//    }
//start
private void openQuestionDialog(int testId) {
    JTextField contentField = new JTextField(20);
    JTextField pointsField = new JTextField(5);

    JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
    panel.add(new JLabel("Question Content:"));
    panel.add(contentField);
    panel.add(new JLabel("Points:"));
    panel.add(pointsField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Add Question",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String content = contentField.getText().trim();
        String pointsText = pointsField.getText().trim();

        if (content.isEmpty() || pointsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int points = Integer.parseInt(pointsText);

            Question question = new Question();
            question.setContent(content);
            question.setPoints(points);
            question.setTestId(testId);

            boolean success = questionService.addQuestion(question);
            if (success) {
                JOptionPane.showMessageDialog(this, "Question added successfully!");
                loadTests(); // Refresh UI
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add question.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Points must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    //end
    private void openOptionDialog(int questionId) {
        JTextField contentField = new JTextField(20);
        JCheckBox correctCheck = new JCheckBox("Is Correct?");
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(contentField);
        panel.add(correctCheck);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Option", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !contentField.getText().trim().isEmpty()) {
            Option option = new Option();
            option.setQuestionId(questionId);
            option.setContent(contentField.getText());
            option.setCorrect(correctCheck.isSelected());

            try {
                boolean success = optionService.addOption(option);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Option added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTests();
                }
            } catch (Exception e) {
                if (e.getMessage().contains("Only one correct option is allowed")) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Cannot add another correct option. This question already has a correct option. Please edit or delete the existing correct option first.",
                            "Multiple Correct Options Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to add option: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else if (contentField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Option content cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTest(int testId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this test?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            testService.deleteTest(testId);
            loadTests();
        }
    }

    private void deleteQuestion(int testId, int questionId) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this question and all its options?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            questionService.deleteQuestion(questionId);
            loadTests();
        }
    }

    private void deleteOption(int questionId, int optionId) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this option?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            optionService.deleteOption(optionId);
            loadTests();
        }
    }
}