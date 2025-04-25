package service;

import models.Enrollment;
import rmi.EnrollmentRemoteService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class EnrollmentServiceClient {
    private EnrollmentRemoteService enrollmentService;

    public EnrollmentServiceClient() {
        try {
            enrollmentService = (EnrollmentRemoteService) Naming.lookup("rmi://localhost:1099/EnrollmentService");
        } catch (Exception e) {
            System.err.println("Error connecting to EnrollmentRemoteService: " + e.getMessage());
        }
    }

    public boolean enrollStudent(Enrollment enrollment) {
        try {
            enrollmentService.enrollStudent(enrollment);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during enrollStudent: " + e.getMessage());
            return false;
        }
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        try {
            return enrollmentService.getEnrollmentsByStudent(studentId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getEnrollmentsByStudent: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteEnrollment(int id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteEnrollment: " + e.getMessage());
            return false;
        }
    }

    public int getCourseCountByStudentId(int studentId) {
        try {
            return enrollmentService.getCourseCountByStudentId(studentId);
        }catch (RemoteException e) {
            System.err.println("RemoteException during getCourseCountByStudentId: " + e.getMessage());
            return 0;
        }
    }
}
