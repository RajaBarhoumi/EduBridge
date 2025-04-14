package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OnboardingSlider extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private int slideIndex = 0;
    private final int totalSlides = 3;

    JButton nextButton = new JButton("Next →");
    JButton prevButton = new JButton("← Previous");

    public OnboardingSlider() {
        setTitle("EduBridge Onboarding");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        cardPanel.add(createStudentSlide(), "slide0");
        cardPanel.add(createProfessorSlide(), "slide1");
        cardPanel.add(createRoleSelector(), "slide2");


        prevButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        prevButton.setFocusPainted(false);
        nextButton.setFocusPainted(false);


        prevButton.addActionListener((ActionEvent e) -> {
            if (slideIndex > 0) {
                slideIndex--;
                cardLayout.show(cardPanel, "slide" + slideIndex);
                updateButtonColors();
            }
        });

        nextButton.addActionListener((ActionEvent e) -> {
            if (slideIndex < totalSlides - 1) {
                slideIndex++;
                cardLayout.show(cardPanel, "slide" + slideIndex);
                updateButtonColors();
            }
        });


        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navigationPanel.setBackground(Color.WHITE);
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        updateButtonColors();
    }

    private JPanel createStudentSlide() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("🎓 Your Path to Knowledge Starts Here", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(33, 33, 99));

        JTextArea desc = new JTextArea("Explore exciting courses, pass QCM tests, and earn certificates.\nTrack your growth and challenge yourself.");
        styleText(desc);

        JLabel image = new JLabel(new ImageIcon("./assets/student.png"), SwingConstants.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(image, BorderLayout.CENTER);
        panel.add(desc, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createProfessorSlide() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(232, 234, 246));

        JLabel title = new JLabel("🧑‍🏫 Shape Minds. Build Futures.", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(63, 81, 181));

        JTextArea desc = new JTextArea("As a professor, manage your courses, create QCM tests,\nand evaluate your students' performance.");
        styleText(desc);

        JLabel image = new JLabel(new ImageIcon("./assets/teacher.png"), SwingConstants.CENTER); // Replace with your uploaded image

        panel.add(title, BorderLayout.NORTH);
        panel.add(image, BorderLayout.CENTER);
        panel.add(desc, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRoleSelector() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBackground(Color.white);

        JLabel title = new JLabel("Let's Get Started!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(25, 118, 210));

        JButton studentBtn = new JButton("Continue as Student");
        JButton professorBtn = new JButton("Continue as Professor");

        styleButton(studentBtn, new Color(100, 181, 246));
        styleButton(professorBtn, new Color(121, 134, 203));

        JLabel hint = new JLabel("Already have an account? Log in", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 14));
        hint.setForeground(Color.GRAY);

        studentBtn.addActionListener(e -> navigateToSignUp("Student"));
        professorBtn.addActionListener(e -> navigateToSignUp("Professor"));

        panel.add(title);
        panel.add(studentBtn);
        panel.add(professorBtn);
        panel.add(hint);

        return panel;
    }

    private void navigateToSignUp(String role) {
        SignUpScreen signUpScreen = new SignUpScreen(role);
        signUpScreen.setVisible(true);
        this.setVisible(false);
    }

    private void styleText(JTextArea area) {
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setOpaque(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 16));
        area.setMargin(new Insets(20, 30, 20, 30));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.white);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
    }

    private void updateButtonColors() {
        nextButton.setEnabled(true);
        prevButton.setEnabled(true);

        if (slideIndex == 0) {
            nextButton.setText("Next →");
            nextButton.setBackground(new Color(33, 150, 243));
            nextButton.setForeground(Color.WHITE);
            prevButton.setEnabled(false);
            prevButton.setBackground(new Color(200, 200, 200));
            prevButton.setForeground(Color.DARK_GRAY);
        } else if (slideIndex == 1) {
            nextButton.setText("Next →");
            nextButton.setBackground(new Color(103, 58, 183));
            nextButton.setForeground(Color.WHITE);
            prevButton.setBackground(new Color(144, 164, 174));
            prevButton.setForeground(Color.WHITE);
        } else if (slideIndex == 2) {
            nextButton.setText("✓ Done");
            nextButton.setEnabled(false);
            nextButton.setBackground(new Color(189, 189, 189));
            nextButton.setForeground(Color.DARK_GRAY);
            prevButton.setBackground(new Color(121, 134, 203));
            prevButton.setForeground(Color.WHITE);
        }
    }


}
