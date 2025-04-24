package ui.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import models.Question;
import models.Option;
import models.Answer;
import service.AnswerServiceClient;
import service.OptionServiceClient;

public class TestScreen extends JFrame {
    private final int testId, studentTestId;
    private final List<Question> questions;
    private int currentQuestionIndex = 0;
    private JLabel questionLabel, timerLabel;
    private ButtonGroup optionsGroup;
    private JPanel optionsPanel;
    private JButton nextButton;

    private Timer questionTimer;
    private final int TIME_LIMIT = 15;
    private int remainingTime;

    public TestScreen(int testId, int studentTestId, List<Question> questions) {
        this.testId = testId;
        this.studentTestId = studentTestId;
        this.questions = questions;
        setupUI();
        loadQuestion();
    }

    private void setupUI() {
        setTitle("Test In Progress");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));

        timerLabel = new JLabel("Time left: ", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1));

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> submitAnswerAndNext());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(timerLabel, BorderLayout.EAST);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);
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

        startTimer();
    }

    private void startTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        remainingTime = TIME_LIMIT;
        timerLabel.setText("Time left: " + remainingTime + "s");

        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    remainingTime--;
                    timerLabel.setText("Time left: " + remainingTime + "s");

                    if (remainingTime <= 0) {
                        questionTimer.cancel();
                        submitAnswerAndNext();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void submitAnswerAndNext() {
        questionTimer.cancel();

        String selectedOptionId = optionsGroup.getSelection() != null
                ? optionsGroup.getSelection().getActionCommand()
                : null;

        if (selectedOptionId != null) {
            Answer answer = new Answer();
            answer.setStudentTestId(studentTestId);
            answer.setQuestionId(questions.get(currentQuestionIndex).getId());
            answer.setSelectedOptionId(Integer.parseInt(selectedOptionId));

            AnswerServiceClient answerService = new AnswerServiceClient();
            answerService.saveAnswer(answer);
        }

        currentQuestionIndex++;
        loadQuestion();
    }

    private void finishTest() {
        JOptionPane.showMessageDialog(this, "Test completed! Your answers have been submitted.");
        dispose();
    }
}
