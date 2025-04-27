package service;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

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

    public int addStudentTest(StudentTest studentTest) {
        try {
            return studentTestService.addStudentTest(studentTest);
        } catch (RemoteException e) {
            System.err.println("RemoteException during addStudentTest: " + e.getMessage());
            return -1;
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

    public String calculateAndUpdateStudentTestScore(int studentTestId) {
        try {
            return studentTestService.calculateAndUpdateStudentTestScore(studentTestId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during calculateAndUpdateStudentTestScore: " + e.getMessage());
            return null;
        }
    }

    public int getCertificateCountByProfessorId(int professorId){
        try {
            return studentTestService.getCertificateCountByProfessorId(professorId);
        }catch (RemoteException e){
            System.err.println("RemoteException during getCertificateCountByProfessorId: " + e.getMessage());
            return -1;
        }
    }

    public int getTestCountByStudentId(int studentId){
        try{
            return studentTestService.getTestCountByStudentId(studentId);
        }catch (RemoteException e){
            System.err.println("RemoteException during getTestCountByStudentId: " + e.getMessage());
            return -1;
        }
    }

    public int getCertificateCountByStudentId(int studentId) {
        try {
            return studentTestService.getCertificateCountByStudentId(studentId);
        }catch (RemoteException e){
            System.err.println("RemoteException during getCertificateCountByStudentId: " + e.getMessage());
            return -1;
        }
    }

    public List<Map<String, Object>> getStudentTestResults(int studentId) {
        try{
            return studentTestService.getStudentTestResults(studentId);
        }catch (RemoteException e){
            System.err.println("RemoteException during getStudentTestResults: " + e.getMessage());
            return null;
        }
    }


}
