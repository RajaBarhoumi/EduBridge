package service;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import models.StudentTest;
import rmi.StudentTestRemoteService;

public class StudentTestServiceClient {
    private StudentTestRemoteService studentTestService;

    public StudentTestServiceClient() {
        try {
            studentTestService = (StudentTestRemoteService) Naming.lookup("rmi://localhost:1099/StudentTestService");
        } catch (Exception e) {
            System.err.println("Error connecting to StudentTestRemoteService: " + e.getMessage());
        }
    }

    public boolean addStudentTest(StudentTest studentTest) {
        try {
            studentTestService.addStudentTest(studentTest);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addStudentTest: " + e.getMessage());
            return false;
        }
    }

    public StudentTest getStudentTestById(int id) {
        try {
            return studentTestService.getStudentTestById(id);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getStudentTestById: " + e.getMessage());
            return null;
        }
    }

    public List<StudentTest> getStudentTestsByStudentId(int studentId) {
        try {
            return studentTestService.getStudentTestsByStudentId(studentId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getStudentTestsByStudentId: " + e.getMessage());
            return null;
        }
    }

    public List<StudentTest> getStudentTestsByTestId(int testId) {
        try {
            return studentTestService.getStudentTestsByTestId(testId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getStudentTestsByTestId: " + e.getMessage());
            return null;
        }
    }

    public boolean updateStudentTest(StudentTest studentTest) {
        try {
            studentTestService.updateStudentTest(studentTest);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateStudentTest: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudentTest(int studentTestId) {
        try {
            studentTestService.deleteStudentTest(studentTestId);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteStudentTest: " + e.getMessage());
            return false;
        }
    }
}
