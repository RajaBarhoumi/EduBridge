package ui.professor;

import service.CourseServiceClient;
import service.StudentTestServiceClient;
import service.TestServiceClient;

import javax.swing.SwingUtilities;
import java.util.Map;

public class ProfessorDashboardHandler extends Thread {
    private final ProfessorDashboard dashboard;
    private final CourseServiceClient courseServiceClient;
    private final StudentTestServiceClient studentTestServiceClient;
    private final TestServiceClient testServiceClient;
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
                // Fetch statistics
                final int courseCount = courseServiceClient.getCourseCountByProfessorId(dashboard.getProfessorId());
                final int testCount = testServiceClient.getTestCountByProfessorId(dashboard.getProfessorId());
                final int certificateCount = studentTestServiceClient.getCertificateCountByProfessorId(dashboard.getProfessorId());
                final Map<String, Integer> passRates = courseServiceClient.getPassRateDistributionByCourse(dashboard.getProfessorId());

                // Update UI on Event Dispatch Thread
                SwingUtilities.invokeLater(() -> {
                    dashboard.updateCourseCount(courseCount);
                    dashboard.updateTestCount(testCount);
                    dashboard.updateCertificateCount(certificateCount);
                    dashboard.updatePassRateChart(passRates);
                });

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                if (!running) {
                    System.out.println("Dashboard updater thread stopped.");
                } else {
                    System.err.println("Thread interrupted unexpectedly: " + e.getMessage());
                }
                break;
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
