package ui.professor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class ProfessorDashboard extends JFrame {

    private int professorId;

    public ProfessorDashboard(int professorId) {
        this.professorId = professorId;
        setTitle("Professor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient top panel
        JPanel topPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(63, 81, 181),
                        getWidth(), getHeight(), new Color(103, 58, 183));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setPreferredSize(new Dimension(getWidth(), 150));
        topPanel.setLayout(new GridLayout(1, 4, 20, 20));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Summary Cards
        topPanel.add(createSummaryCard("📘 Courses", "12", () -> {
            new ProfessorCourseManager(professorId).setVisible(true);
        }));

        topPanel.add(createSummaryCard("📝 Tests", "25", () -> {
            new ProfessorTestManager(professorId).setVisible(true);
        }));
        topPanel.add(createSummaryCard("📈 Avg Pass", "82%", null));
        topPanel.add(createSummaryCard("🎓 Certificates", "130", null));

        // Chart Panel
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        chartPanel.add(createPieChart(), BorderLayout.CENTER);

        // Quick Actions Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton createCourseBtn = new JButton("➕ Create Course");
        JButton createTestBtn = new JButton("➕ Create Test");

        createCourseBtn.addActionListener(e -> {
            //new AddCourseForm(professorId).setVisible(true);
        });

        styleButton(createCourseBtn);
        styleButton(createTestBtn);
        actionPanel.add(createCourseBtn);
        actionPanel.add(createTestBtn);

        // Add to frame
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createSummaryCard(String title, String value, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(100, 181, 246));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(200, 100));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        if (onClick != null) {
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                }
            });
        }

        return card;
    }

    private Component createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Java Basics", 40);
        dataset.setValue("OOP", 25);
        dataset.setValue("Databases", 15);
        dataset.setValue("Algorithms", 20);

        JFreeChart chart = ChartFactory.createPieChart(
                "Student Pass Rate by Course",
                dataset,
                true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Java Basics", new Color(63, 81, 181));
        plot.setSectionPaint("OOP", new Color(103, 58, 183));
        plot.setSectionPaint("Databases", new Color(100, 181, 246));
        plot.setSectionPaint("Algorithms", new Color(144, 202, 249));
        chart.setBackgroundPaint(Color.WHITE);

        return new ChartPanel(chart);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(63, 81, 181));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
    }
}
