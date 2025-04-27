package ui.professor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import service.CourseServiceClient;
import service.StudentTestServiceClient;
import service.TestServiceClient;
import ui.auth.LoginScreen;
import ui.onboarding.OnboardingSlider;

public class ProfessorDashboard extends JFrame {

    private int professorId;
    private CourseServiceClient courseServiceClient;
    private StudentTestServiceClient studentTestServiceClient;
    private TestServiceClient testServiceClient;
    private ProfessorDashboardHandler statsUpdaterThread;
    private JLabel courseCountLabel;
    private JLabel testCountLabel;
    private JLabel certificateCountLabel;
    private JLabel studentsTestingLabel;
    private ChartPanel passRateChartPanel;

    public ProfessorDashboard(int professorId) {
        this.courseServiceClient = new CourseServiceClient();
        this.studentTestServiceClient = new StudentTestServiceClient();
        this.testServiceClient = new TestServiceClient();
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
        topPanel.setLayout(new BorderLayout()); // <--- Change this!
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

// Back button
        ImageIcon backIcon = new ImageIcon(ProfessorDashboard.class.getClassLoader().getResource("logout.png"));
        Image img = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton backButton = new JButton(new ImageIcon(img));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            LoginScreen screen = new LoginScreen();
            screen.setVisible(true);
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);

// Cards Panel
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 20px horizontal gap
        cardsPanel.setOpaque(false);

        int courseCount = courseServiceClient.getCourseCountByProfessorId(professorId);
        int testCount = testServiceClient.getTestCountByProfessorId(professorId);
        int certificateCount = studentTestServiceClient.getCertificateCountByProfessorId(professorId);

        cardsPanel.add(createSummaryCard("📘 Courses", String.valueOf(courseCount), () -> {
            new ProfessorCourseManager(professorId).setVisible(true);
            dispose();
        }, "course"));

        cardsPanel.add(createSummaryCard("📝 Tests", String.valueOf(testCount), () -> {
            new ProfessorTestManager(professorId).setVisible(true);
            dispose();
        }, "test"));

        cardsPanel.add(createSummaryCard("🎓 Certificates", String.valueOf(certificateCount), null, "certificate"));

// Add panels to topPanel
        topPanel.add(backButtonPanel, BorderLayout.WEST);
        topPanel.add(cardsPanel, BorderLayout.CENTER);




        // Chart Panel
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        passRateChartPanel = createPieChart();
        chartPanel.add(passRateChartPanel, BorderLayout.CENTER);

        // Quick Actions Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton createCourseBtn = new JButton("➕ Create Course");
        JButton createTestBtn = new JButton("➕ Create Test");

        createCourseBtn.addActionListener(e -> {
            new ProfessorCourseManager(professorId).setVisible(true);
        });

        createTestBtn.addActionListener(e -> {
            new ProfessorTestManager(professorId).setVisible(true);
        });

        styleButton(createCourseBtn);
        styleButton(createTestBtn);
        actionPanel.add(createCourseBtn);
        actionPanel.add(createTestBtn);

        // Add to frame
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        statsUpdaterThread = new ProfessorDashboardHandler(this, courseServiceClient, studentTestServiceClient, testServiceClient);
        statsUpdaterThread.start();

        setVisible(true);
    }

    public int getProfessorId() {
        return professorId;
    }

    public void updateCourseCount(int newCount) {
        if (courseCountLabel != null) {
            courseCountLabel.setText(String.valueOf(newCount));
        }
    }

    public void updateTestCount(int newCount) {
        if (testCountLabel != null) {
            testCountLabel.setText(String.valueOf(newCount));
        }
    }

    public void updateCertificateCount(int newCount) {
        if (certificateCountLabel != null) {
            certificateCountLabel.setText(String.valueOf(newCount));
        }
    }


    public void updatePassRateChart(Map<String, Integer> passRates) {
        if (passRateChartPanel != null) {
            DefaultPieDataset dataset = new DefaultPieDataset();
            if (passRates != null) {
                for (Map.Entry<String, Integer> entry : passRates.entrySet()) {
                    dataset.setValue(entry.getKey(), entry.getValue());
                }
            }
            JFreeChart chart = ChartFactory.createPieChart(
                    "Student Pass Rate by Course",
                    dataset,
                    true, true, false);
            PiePlot plot = (PiePlot) chart.getPlot();
            Color[] colors = {new Color(63, 81, 181), new Color(103, 58, 183),
                    new Color(100, 181, 246), new Color(144, 202, 249)};
            int i = 0;
            for (Object key : dataset.getKeys()) {
                plot.setSectionPaint((Comparable) key, colors[i % colors.length]);
                i++;
            }
            chart.setBackgroundPaint(Color.WHITE);
            passRateChartPanel.setChart(chart);
        }
    }

    @Override
    public void dispose() {
        if (statsUpdaterThread != null) {
            statsUpdaterThread.stopThread();
        }
        super.dispose();
    }

    private JPanel createSummaryCard(String title, String value, Runnable onClick, String labelType) {
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

        // Store label reference based on type
        if ("course".equals(labelType)) {
            courseCountLabel = valueLabel;
        } else if ("test".equals(labelType)) {
            testCountLabel = valueLabel;
        } else if ("certificate".equals(labelType)) {
            certificateCountLabel = valueLabel;
        } else if ("studentsTesting".equals(labelType)) {
            studentsTestingLabel = valueLabel;
        }

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

    private ChartPanel createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Map<String, Integer> passRates = courseServiceClient.getPassRateDistributionByCourse(professorId);
        if (passRates != null) {
            for (Map.Entry<String, Integer> entry : passRates.entrySet()) {
                dataset.setValue(entry.getKey(), entry.getValue());
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Student Pass Rate by Course",
                dataset,
                true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        Color[] colors = {new Color(63, 81, 181), new Color(103, 58, 183),
                new Color(100, 181, 246), new Color(144, 202, 249)};
        int i = 0;
        for (Object key : dataset.getKeys()) {
            plot.setSectionPaint((Comparable) key, colors[i % colors.length]);
            i++;
        }

        chart.setBackgroundPaint(Color.WHITE);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        return chartPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(63, 81, 181));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
    }
}