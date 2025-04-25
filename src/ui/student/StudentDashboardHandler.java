package ui.student;

import service.EnrollmentServiceClient;
import service.StudentTestServiceClient;

public class StudentDashboardHandler extends Thread {
    private StudentDashboard dashboard;
    private EnrollmentServiceClient enrollmentServiceClient;
    private StudentTestServiceClient studentTestServiceClient;
    private volatile boolean running = true;

    public StudentDashboardHandler(StudentDashboard dashboard,
                                   EnrollmentServiceClient enrollmentServiceClient,
                                   StudentTestServiceClient studentTestServiceClient) {
        this.dashboard = dashboard;
        this.enrollmentServiceClient = enrollmentServiceClient;
        this.studentTestServiceClient = studentTestServiceClient;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Fetch all statistics
                int courseCount = enrollmentServiceClient.getCourseCountByStudentId(dashboard.getStudentId());
                int testCount = studentTestServiceClient.getTestCountByStudentId(dashboard.getStudentId());
                int certificateCount = studentTestServiceClient.getCertificateCountByStudentId(dashboard.getStudentId());

                // Update dashboard on Event Dispatch Thread
                javax.swing.SwingUtilities.invokeLater(() -> {
                    dashboard.updateCourseCount(courseCount);
                    dashboard.updateTestCount(testCount);
                    dashboard.updateCertificateCount(certificateCount);
                });

                // Sleep for 30 seconds
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                running = false; // Exit on interrupt
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