package ui.auth;

import models.User;
import service.UserServiceClient;
import ui.professor.ProfessorDashboard;
import ui.student.StudentDashboard;
import ui.onboarding.OnboardingSlider;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private String role;
    private JFrame previousWindow;
    public LoginScreen(String role) {
        this.role = role;
    }
//    public LoginScreen(JFrame previousWindow) {
//        this.previousWindow = previousWindow;
//    }
public LoginScreen() {
    setTitle("EduBridge - Log In");
    setSize(450, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Use BorderLayout for the main layout
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(new Color(240, 248, 255));
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    // 🖼️ Add logo/image
    ImageIcon icon = new ImageIcon(LoginScreen.class.getClassLoader().getResource("login.png"));
    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    JLabel imageLabel = new JLabel(new ImageIcon(image));
    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

    JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
    title.setFont(new Font("SansSerif", Font.BOLD, 24));
    title.setForeground(new Color(33, 33, 99));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    title.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

    JTextField emailField = styledTextField("Email");
    JPasswordField passwordField = styledPasswordField("Password");

    JButton loginBtn = new JButton("Log In");
    stylePrimaryButton(loginBtn, new Color(63, 81, 181));

    loginBtn.addActionListener(e -> {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Initialize the service client and attempt login
        UserServiceClient serviceClient = new UserServiceClient();
        User user = serviceClient.login(email, password);

        if (user != null) {
            // Print role for debugging
            System.out.println("ROLE RECEIVED: [" + user.getRole() + "]");

            String userRole = String.valueOf(user.getRole());
            if (userRole != null) {
                switch (userRole.toLowerCase()) {
                    case "student":
                        System.out.println("Logged in as " + user.getEmail());
                        SwingUtilities.invokeLater(() -> {
                            new StudentDashboard(user.getId()).setVisible(true);
                        });
                        break;
                    case "professor":
                        System.out.println("Logged in as " + user.getEmail());
                        SwingUtilities.invokeLater(() -> {
                            new ProfessorDashboard(user.getId()).setVisible(true);
                        });
                        break;
                    default:
                        System.out.println("Unknown role: " + userRole);
                        JOptionPane.showMessageDialog(this, "Unknown role: " + userRole, "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Exit early so we don't dispose
                }
                dispose(); // Close the login screen only if role is valid
            } else {
                System.out.println("Role is null.");
                JOptionPane.showMessageDialog(this, "User role is not defined.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Login failed
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    });

    // ➕ Add components to main panel
    mainPanel.add(imageLabel);
    mainPanel.add(title);
    mainPanel.add(emailField);
    mainPanel.add(passwordField);
    mainPanel.add(Box.createVerticalStrut(20));
    mainPanel.add(loginBtn);

    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

    // 🏹 Back Button (now in the BorderLayout)
    ImageIcon backIcon = new ImageIcon(LoginScreen.class.getClassLoader().getResource("back_arrow.png"));
    Image img = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    JButton backButton = new JButton(new ImageIcon(img));
    backButton.setBorderPainted(false);
    backButton.setContentAreaFilled(false);
    backButton.setFocusPainted(false);
    backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


    backButton.addActionListener(e -> {
        dispose(); // Close the current login window
        OnboardingSlider onboardingScreen = new OnboardingSlider();
        onboardingScreen.setVisible(true);

        // Set the slide index to the last slide (role selection slide)
        onboardingScreen.setSlideIndex(2); // 2 corresponds to the last slide (indexing starts from 0)
    });
    // Add back button to the top left
    JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    backPanel.setBackground(new Color(240, 248, 255));
    backPanel.add(backButton);

    // Add backPanel and mainPanel to the frame
    add(backPanel, BorderLayout.NORTH);
    add(mainPanel, BorderLayout.CENTER);
}


    private JTextField styledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private JPasswordField styledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private void stylePrimaryButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
    }
}

