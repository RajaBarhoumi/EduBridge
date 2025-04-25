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

    public ProfessorTestManager(int professorId) {
        this.professorId = professorId;

        setTitle("Test Manager");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        initComponents();
    }

    private void initComponents() {
        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(255, 255, 255));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        courseComboBox = new JComboBox<>();
        courseComboBox.setPreferredSize(new Dimension(200, 30));
        courseComboBox.addActionListener(e -> loadTests());

        JButton addTestBtn = createStyledButton("Add Test", new Color(33, 150, 243));
        addTestBtn.addActionListener(e -> openTestDialog(false));

        topPanel.add(new JLabel("Select Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(addTestBtn);

        testContainer = new JPanel();
        testContainer.setLayout(new BoxLayout(testContainer, BoxLayout.Y_AXIS));
        testContainer.setBackground(new Color(245, 245, 245));
        testScrollPane = new JScrollPane(testContainer);
        testScrollPane.setBorder(null);

        add(topPanel, BorderLayout.NORTH);
        add(testScrollPane, BorderLayout.CENTER);

        loadCourses();
    }

    private void loadCourses() {
        courseComboBox.removeAllItems();
        List<Course> courses = courseService.getAllCourses();
        for (Course course : courses) {
            courseComboBox.addItem(course);
        }
    }

    private void openTestDialog(boolean isUpdate) {
        openTestDialog(isUpdate, null);
    }

    private void loadTests() {
        testContainer.removeAll();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        if (selectedCourse == null) return;

        List<Test> tests = testService.getTestsByCourseId(selectedCourse.getId());
        for (Test test : tests) {
            testContainer.add(createTestCard(test));
            testContainer.add(Box.createVerticalStrut(10));
        }

        testContainer.revalidate();
        testContainer.repaint();
    }

    private JPanel createTestCard(Test test) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Card Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 250, 250));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(test.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(250, 250, 250));
        JButton updateBtn = createStyledButton("Update", new Color(255, 193, 7));
        JButton deleteBtn = createStyledButton("Delete", new Color(244, 67, 54));
        JButton addQuestionBtn = createStyledButton("Add Question", new Color(76, 175, 80));

        updateBtn.addActionListener(e -> openTestDialog(true, test));
        deleteBtn.addActionListener(e -> deleteTest(test.getId()));
        addQuestionBtn.addActionListener(e -> openQuestionDialog(test.getId()));

        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(addQuestionBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        JPanel questionsContent = new JPanel();
        questionsContent.setLayout(new BoxLayout(questionsContent, BoxLayout.Y_AXIS));
        questionsContent.setBackground(Color.WHITE);

        List<Question> questions = questionService.getQuestionsByTest(test.getId());
        for (Question question : questions) {
            questionsContent.add(createQuestionPanel(test.getId(), question));
            questionsContent.add(Box.createVerticalStrut(30));
        }

        // Make questions scrollable
        JScrollPane questionsScrollPane = new JScrollPane(questionsContent);
        questionsScrollPane.setBorder(null);
        questionsScrollPane.setPreferredSize(new Dimension(0, Math.min(questions.size() * 100, 400)));
        questionsScrollPane.setVisible(false);

        JButton toggleBtn = createStyledButton("Show Questions", new Color(100, 100, 100));
        toggleBtn.addActionListener(e -> {
            questionsScrollPane.setVisible(!questionsScrollPane.isVisible());
            toggleBtn.setText(questionsScrollPane.isVisible() ? "Hide Questions" : "Show Questions");
            card.revalidate();
        });

        card.add(header, BorderLayout.NORTH);
        card.add(questionsScrollPane, BorderLayout.CENTER);
        card.add(toggleBtn, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createQuestionPanel(int testId, Question question) {
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(new Color(245, 245, 245));
        questionPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel questionLabel = new JLabel(question.getContent() + " (Points: " + question.getPoints() + ")");
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton deleteQuestionBtn = createStyledButton("Delete", new Color(244, 67, 54));
        deleteQuestionBtn.addActionListener(e -> deleteQuestion(testId, question.getId()));

        JPanel questionHeader = new JPanel(new BorderLayout());
        questionHeader.setBackground(new Color(245, 245, 245));
        questionHeader.add(questionLabel, BorderLayout.WEST);
        questionHeader.add(deleteQuestionBtn, BorderLayout.EAST);

        JPanel optionsContent = new JPanel();
        optionsContent.setLayout(new BoxLayout(optionsContent, BoxLayout.Y_AXIS));
        optionsContent.setBackground(new Color(245, 245, 245));

        List<Option> options = optionService.getOptionsByQuestion(question.getId());
        if (options != null) {
            for (Option option : options) {
                optionsContent.add(createOptionPanel(question.getId(), option));
                optionsContent.add(Box.createVerticalStrut(20));
            }
        }

        JScrollPane optionsScrollPane = new JScrollPane(optionsContent);
        optionsScrollPane.setBorder(null);
        optionsScrollPane.setPreferredSize(new Dimension(0, Math.min((options != null ? options.size() : 0) * 50, 150)));

        JButton addOptionBtn = createStyledButton("Add Option", new Color(76, 175, 80));
        addOptionBtn.addActionListener(e -> openOptionDialog(question.getId()));

        questionPanel.add(questionHeader, BorderLayout.NORTH);
        questionPanel.add(optionsScrollPane, BorderLayout.CENTER);
        questionPanel.add(addOptionBtn, BorderLayout.SOUTH);

        return questionPanel;
    }

    private JPanel createOptionPanel(int questionId, Option option) {
        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.setBackground(Color.WHITE);
        optionPanel.setBorder(new EmptyBorder(2, 10, 2, 10));

        JLabel optionLabel = new JLabel(option.getContent() + (option.isCorrect() ? " (Correct)" : ""));
        optionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton deleteOptionBtn = createStyledButton("Delete", new Color(244, 67, 54));
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
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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
        panel.add(new JLabel("Max Attempts:"));

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
            optionService.addOption(option);
            loadTests();
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
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            questionService.deleteQuestion(questionId);
            loadTests();
        }
    }

    private void deleteOption(int questionId, int optionId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this option?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            optionService.deleteOption(optionId);
            loadTests();
        }
    }

}