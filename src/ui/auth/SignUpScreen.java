package ui.auth;

import service.UserServiceClient;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class SignUpScreen extends JFrame {
    private String role;

    public SignUpScreen(String role) {
        this.role = role;
        setTitle("EduBridge - Sign Up");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        ImageIcon icon = new ImageIcon(SignUpScreen.class.getClassLoader().getResource("login.png"));
        Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel title = new JLabel("Create Your Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 99));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JTextField nameField = styledTextField("Full Name");
        JTextField emailField = styledTextField("Email");
        JPasswordField passwordField = styledPasswordField("Password");

        JButton registerBtn = new JButton("Sign Up");
        stylePrimaryButton(registerBtn, new Color(63, 81, 181));

        JLabel loginLink = new JLabel("Already have an account? Log in →");
        loginLink.setFont(new Font("SansSerif", Font.ITALIC, 14));
        loginLink.setForeground(Color.GRAY);
        loginLink.setHorizontalAlignment(SwingConstants.CENTER);
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLink.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Input validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(this, "Name must be at least 3 characters and contain only letters and spaces",
                        "Invalid Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address",
                        "Invalid Email", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(this,
                        "Password must be at least 5 characters and contain at least one letter and one number",
                        "Weak Password", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Call the register method from UserServiceClient
            UserServiceClient userServiceClient = new UserServiceClient();
            boolean success = userServiceClient.register(name, email, password, this.role);

            // If the registration failed, check for the specific error
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginScreen().setVisible(true);
            } else {
                // Here, handle the error if the email already exists
                JOptionPane.showMessageDialog(this, "This email is already registered. Please try a different one.",
                        "Email Already Exists", JOptionPane.ERROR_MESSAGE);
            }
        });


        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new LoginScreen().setVisible(true);
            }
        });

        mainPanel.add(imageLabel);
        mainPanel.add(title);
        mainPanel.add(nameField);
        mainPanel.add(emailField);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(registerBtn);
        mainPanel.add(loginLink);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        add(mainPanel);
    }

    // Validation methods
    private boolean isValidName(String name) {
        // Name should be at least 3 characters and contain only letters and spaces
        return name.length() >= 3 && Pattern.matches("^[a-zA-Z\\s]+$", name);
    }

    private boolean isValidEmail(String email) {
        // Basic email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPassword(String password) {
        // Password should be at least 8 characters and contain at least one letter and one number
        return password.length() >= 5 &&
                Pattern.matches(".*[a-zA-Z].*", password) &&
                Pattern.matches(".*\\d.*", password);
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