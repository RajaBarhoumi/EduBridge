package pages;

import Database.UserManager;

import javax.swing.*;
import java.awt.*;

// In SignUpScreen class
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

        ImageIcon icon = new ImageIcon("./assets/login.png"); // Adjust path if needed
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
        stylePrimaryButton(registerBtn, new Color(63, 81, 181)); // Deep indigo

        JLabel loginLink = new JLabel("Already have an account? Log in →");
        loginLink.setFont(new Font("SansSerif", Font.ITALIC, 14));
        loginLink.setForeground(Color.GRAY);
        loginLink.setHorizontalAlignment(SwingConstants.CENTER);
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLink.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            // Call insertUser from UserManager to insert the user into the database
            UserManager userManager = new UserManager();
            int result = userManager.insertUser(name, email, password, this.role);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Try again.");
            }
        });

        // Add components
        mainPanel.add(imageLabel);
        mainPanel.add(title);
        mainPanel.add(nameField);
        mainPanel.add(emailField);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(registerBtn);
        mainPanel.add(loginLink);

        // Padding around form
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        add(mainPanel);
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

