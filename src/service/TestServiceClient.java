package service;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import models.Test;
import rmi.TestRemoteService;

public class TestServiceClient {
    private TestRemoteService testService;

    public TestServiceClient() {
        try {
            testService = (TestRemoteService) Naming.lookup("rmi://localhost:1099/TestService");
        } catch (Exception e) {
            System.err.println("Error connecting to TestRemoteService: " + e.getMessage());
        }
    }

    public boolean createTest(Test test) {
        try {
            testService.addTest(test);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during createTest: " + e.getMessage());
            return false;
        }
    }

    public List<Test> getTestsByCourseId(int courseId) {
        try {
            return testService.getTestsByCourseId(courseId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getTestsByCourseId: " + e.getMessage());
            return null;
        }
    }

    public Test getTestById(int testId) {
        try {
            return testService.getTestById(testId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getTestById: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteTest(int testId) {
        try {
            testService.deleteTest(testId);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteTest: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTest(Test test) {
        try {
            testService.updateTest(test);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateTest: " + e.getMessage());
            return false;
        }
    }

    public List<Test> getTestsByProfessorId(int professorId) {
        try {
            return testService.getTestsByProfessorId(professorId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getTestsByProfessorId: " + e.getMessage());
            return null;
        }
    }

    public List<Test> getTestsByStudentId(int studentId) {
        try {
            return testService.getTestsByStudentId(studentId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getTestsByStudentId: " + e.getMessage());
            return null;
        }
    }
}
