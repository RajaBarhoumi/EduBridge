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
                int courseCount = enrollmentServiceClient.getCourseCountByStudentId(dashboard.getStudentId());
                int testCount = studentTestServiceClient.getTestCountByStudentId(dashboard.getStudentId());
                int certificateCount = studentTestServiceClient.getCertificateCountByStudentId(dashboard.getStudentId());

                javax.swing.SwingUtilities.invokeLater(() -> {
                    dashboard.updateCourseCount(courseCount);
                    dashboard.updateTestCount(testCount);
                    dashboard.updateCertificateCount(certificateCount);
                });

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                if (!running) {
                    System.out.println("Dashboard updater thread stopped.");
                } else {
                    System.err.println("Thread interrupted unexpectedly: " + e.getMessage());
                }
                break; // Exit the loop
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