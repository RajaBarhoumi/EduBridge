package pages;

import Database.UserManager;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private String role;

    public LoginScreen(String role) {
        this.role = role;
        setTitle("EduBridge - Log In");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 🖼️ Add logo/image
        ImageIcon icon = new ImageIcon("assets/login.png"); // Update the path as needed
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

        JLabel signUpLink = new JLabel("Don't have an account? Sign Up →");
        signUpLink.setFont(new Font("SansSerif", Font.ITALIC, 14));
        signUpLink.setForeground(Color.GRAY);
        signUpLink.setHorizontalAlignment(SwingConstants.CENTER);
        signUpLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpLink.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            UserManager userManager = new UserManager();
            //boolean authenticated = userManager.login(email, password, role);
            /*
            if (authenticated) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                // TODO: Open dashboard or next screen
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
            }

             */
        });

        signUpLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new SignUpScreen(role).setVisible(true);
            }
        });

        // ➕ Add components to panel
        mainPanel.add(imageLabel);
        mainPanel.add(title);
        mainPanel.add(emailField);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(loginBtn);
        mainPanel.add(signUpLink);

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

