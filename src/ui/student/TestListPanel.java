package ui.student;

import models.Question;
import models.StudentTest;
import models.Test;
import service.QuestionServiceClient;
import service.StudentTestServiceClient;
import service.TestServiceClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.List;

public class TestListPanel extends JPanel {
    private int studentId;

    public TestListPanel(int studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());

        TestServiceClient testServiceClient = new TestServiceClient();
        List<Test> testList = testServiceClient.getTestsByStudentId(studentId);

        JLabel titleLabel = new JLabel("Test List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel testPanel = new JPanel();
        testPanel.setLayout(new GridLayout(testList.size(), 1, 10, 10));

        for (Test test : testList) {
            JPanel testCard = new JPanel();
            testCard.setLayout(new BorderLayout());
            testCard.setBackground(new Color(200, 217, 255));
            testCard.setPreferredSize(new Dimension(500, 80));

            JLabel testNameLabel = new JLabel(test.getTitle());
            testNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            testCard.add(testNameLabel, BorderLayout.CENTER);

            JButton startButton = new JButton("Start");
            startButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
            startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            startButton.setBackground(new Color(76, 175, 80));
            startButton.setForeground(Color.WHITE);
            startButton.setPreferredSize(new Dimension(120, 40));

            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TestServiceClient serviceClient = new TestServiceClient();
                    int questionCount = serviceClient.getNumberOfQuestions(test.getId());
                    int timeLimit = test.getTimeLimit(); // assuming minutes

                    String message = String.format(
                            "Test Title: %s\nQuestions: %d\nTime Limit: %d minutes\n\n" +
                                    "To receive a certificate, you must answer at least 80%% of the questions correctly.\n\n" +
                                    "Do you want to start?",
                            test.getTitle(), questionCount, timeLimit
                    );

                    int response = JOptionPane.showConfirmDialog(
                            null,
                            message,
                            "Start Test Confirmation",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        startTest(test, studentId);
                    }
                }
            });

            testCard.add(startButton, BorderLayout.EAST);
            testPanel.add(testCard);
        }

        add(testPanel, BorderLayout.CENTER);
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
            JOptionPane.showMessageDialog(null, "Failed to register test attempt. Please try again.");
            return;
        }

        QuestionServiceClient questionService = new QuestionServiceClient();
        List<Question> questions = questionService.getQuestionsByTest(test.getId());

        if (questions != null && !questions.isEmpty()) {
            TestScreen testScreen = new TestScreen(test, studentTestId, questions);
            testScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No questions found for this test.");
        }
    }


}
