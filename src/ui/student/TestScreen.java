package ui.student;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import models.*;
import service.*;

public class TestScreen extends JFrame {
    private JProgressBar progressBar;
    private int unansweredCount;
    private final int testId, studentTestId;
    private final Test test;
    private final List<Question> questions;
    private int currentQuestionIndex = 0;

    private JLabel questionLabel, timerLabel;
    private ButtonGroup optionsGroup;
    private JPanel optionsPanel;
    private JButton nextButton;

    private Timer testTimer;
    private int remainingTimeInSeconds;

    public TestScreen(Test test, int studentTestId, List<Question> questions) {
        this.testId = test.getId();
        this.test = test;
        this.studentTestId = studentTestId;
        this.questions = questions;

        setupUI();

        this.remainingTimeInSeconds = test.getTimeLimit() * 60;

        startTestTimer();
        loadQuestion();
    }

    private void setupUI() {
        setTitle("Test In Progress");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));

        timerLabel = new JLabel("Time left: ", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        optionsPanel = new JPanel(new GridLayout(0, 1));

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> submitAnswerAndNext());

        unansweredCount = questions.size();
        progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(237, 134, 79));
        progressBar.setBorder(BorderFactory.createTitledBorder("Questions Answered"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(timerLabel, BorderLayout.EAST);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(nextButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);



    }

    private void startTestTimer() {
        testTimer = new Timer();
        updateTimerLabel();

        testTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    remainingTimeInSeconds--;
                    updateTimerLabel();

                    if (remainingTimeInSeconds <= 0) {
                        testTimer.cancel();
                        finishTest();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void updateTimerLabel() {
        int minutes = remainingTimeInSeconds / 60;
        int seconds = remainingTimeInSeconds % 60;
        timerLabel.setText(String.format("Time left: %02d:%02d", minutes, seconds));
    }

    private void loadQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            finishTest();
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + currentQuestion.getContent());

        optionsPanel.removeAll();
        optionsGroup = new ButtonGroup();

        OptionServiceClient optionService = new OptionServiceClient();
        List<Option> options = optionService.getOptionsByQuestion(currentQuestion.getId());

        for (Option opt : options) {
            JRadioButton optionButton = new JRadioButton(opt.getContent());
            optionButton.setActionCommand(String.valueOf(opt.getId()));
            optionsGroup.add(optionButton);
            optionsPanel.add(optionButton);
        }

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void submitAnswerAndNext() {
        if (currentQuestionIndex >= questions.size()) {
            return;
        }

        String selectedOptionId = optionsGroup.getSelection() != null
                ? optionsGroup.getSelection().getActionCommand()
                : null;

        if (selectedOptionId != null) {
            Answer answer = new Answer();
            answer.setStudentTestId(studentTestId);
            answer.setQuestionId(questions.get(currentQuestionIndex).getId());
            answer.setSelectedOptionId(Integer.parseInt(selectedOptionId));

            AnswerServiceClient answerService = new AnswerServiceClient();
            boolean isCorrect = answerService.isAnswerCorrect(Integer.parseInt(selectedOptionId));

            provideAnswerFeedback(isCorrect);
            answerService.saveAnswer(answer);

            int answeredCount = questions.size() - --unansweredCount;
            progressBar.setValue(answeredCount);
            progressBar.setString(answeredCount + " of " + questions.size() + " answered");
        }

        currentQuestionIndex++;

        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (currentQuestionIndex < questions.size()) {
                        loadQuestion();
                    } else {
                        finishTest();
                    }
                });
            }
        }, 1000);
    }

    private void provideAnswerFeedback(boolean isCorrect) {
        if (isCorrect) {
            questionLabel.setForeground(Color.GREEN);
            questionLabel.setText("Correct! " + questionLabel.getText());
        } else {
            questionLabel.setForeground(Color.RED);
            questionLabel.setText("Incorrect! " + questionLabel.getText());
        }
    }

    private void finishTest() {
        if (testTimer != null) {
            testTimer.cancel();
        }

        StudentTestServiceClient studentTestService = new StudentTestServiceClient();
        StudentTest studentTest = studentTestService.getStudentTestById(studentTestId);
        if (studentTest != null) {
            studentTestService.updateStudentTest(studentTest);
        }

        // Call the method and get the result message
        String resultMessage = studentTestService.calculateAndUpdateStudentTestScore(studentTestId);
        System.out.println("Result: " + resultMessage);

        // Split the result message to extract the pass/fail status and score
        String[] resultParts = resultMessage.split(" with score: ");
        String passStatus = resultParts[0];  // "Passed" or "Failed"
        float score = Float.parseFloat(resultParts[1]);

        if ("Passed".equals(passStatus)) {
            System.out.println("Student test passed with score = " + score);
            generateCertificate(studentTest, score);  // Pass the score to the certificate generator
        } else {
            System.err.println("Failed to calculate or update student test score. Score: " + score);
        }

        JOptionPane.showMessageDialog(this, "Test completed or time's up! Your answers have been submitted.");
        dispose();
    }

    private void generateCertificate(StudentTest studentTest, float score) {
        System.out.println("Student test : " + studentTest.toString());
        try {
            User student = new UserServiceClient().getStudentByStudentTestId(studentTestId);
            Course course = new CourseServiceClient().getCourseByTestId(testId);

            File file = new File("certificate.html");
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write("<!DOCTYPE html>");
                writer.write("<html><head><title>Certificate of Completion</title>");
                writer.write("<style>");
                writer.write("body { font-family: 'Arial'; text-align: center; background-color: #f9f9f9; padding: 40px; }");
                writer.write(".certificate { border: 10px solid #eee; padding: 30px; background-color: #fff; }");
                writer.write("h1 { color: #4CAF50; }");
                writer.write("</style>");
                writer.write("</head><body>");
                writer.write("<div class='certificate'>");
                writer.write("<h1>Certificate of Completion</h1>");
                writer.write("<p>This certificate is proudly presented to</p>");
                writer.write("<h2>" + student.getName() + "</h2>");
                writer.write("<p>for successfully completing the course:</p>");
                writer.write("<h3>" + course.getTitle() + "</h3>");
                writer.write("<p>on " + java.time.LocalDate.now() + "</p>");
                writer.write("<p>Score Achieved: " + score + " points</p>");  // Display the score here
                writer.write("<br><br><p><i>EduBridge Academy</i></p>");
                writer.write("</div></body></html>");
            }

            // Open certificate in browser
            Desktop.getDesktop().browse(file.toURI());
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating certificate: " + e.getMessage());
        }
    }

}
