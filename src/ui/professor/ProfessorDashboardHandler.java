package ui.professor;

import service.CourseServiceClient;
import service.StudentTestServiceClient;
import service.TestServiceClient;
import java.util.Map;

public class ProfessorDashboardHandler extends Thread {
    private ProfessorDashboard dashboard;
    private CourseServiceClient courseServiceClient;
    private StudentTestServiceClient studentTestServiceClient;
    private TestServiceClient testServiceClient;
    private volatile boolean running = true;

    public ProfessorDashboardHandler(ProfessorDashboard dashboard,
                                       CourseServiceClient courseServiceClient,
                                       StudentTestServiceClient studentTestServiceClient,
                                       TestServiceClient testServiceClient) {
        this.dashboard = dashboard;
        this.courseServiceClient = courseServiceClient;
        this.studentTestServiceClient = studentTestServiceClient;
        this.testServiceClient = testServiceClient;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Fetch all statistics
                int courseCount = courseServiceClient.getCourseCountByProfessorId(dashboard.getProfessorId());
                int testCount = testServiceClient.getTestCountByProfessorId(dashboard.getProfessorId());
                int certificateCount = studentTestServiceClient.getCertificateCountByProfessorId(dashboard.getProfessorId());
                Map<String, Integer> passRates = courseServiceClient.getPassRateDistributionByCourse(dashboard.getProfessorId());

                javax.swing.SwingUtilities.invokeLater(() -> {
                    dashboard.updateCourseCount(courseCount);
                    dashboard.updateTestCount(testCount);
                    dashboard.updateCertificateCount(certificateCount);
                    dashboard.updatePassRateChart(passRates);
                });

                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                running = false;
            } catch (Exception e) {
                System.err.println("Error fetching dashboard statistics: " + e.getMessage());
            }
        }
    }

    public void stopThread() {
        running = false;
        interrupt();
    }
}