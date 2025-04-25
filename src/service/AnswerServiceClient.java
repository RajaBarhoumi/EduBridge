package service;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import models.Answer;
import rmi.AnswerRemoteService;

public class AnswerServiceClient {
    private AnswerRemoteService answerService;

    public AnswerServiceClient() {
        try {
            answerService = (AnswerRemoteService) Naming.lookup("rmi://localhost:1099/AnswerService");
        } catch (Exception e) {
            System.err.println("Error connecting to AnswerRemoteService: " + e.getMessage());
        }
    }

    public boolean saveAnswer(Answer answer) {
        try {
            answerService.saveAnswer(answer);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during saveAnswer: " + e.getMessage());
            return false;
        }
    }

    public List<Answer> getAnswersByStudentAndTest(int studentId, int testId) {
        try {
            return answerService.getAnswersByStudentAndTest(studentId, testId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getAnswersByStudentAndTest: " + e.getMessage());
            return null;
        }
    }

    public boolean isAnswerCorrect(int selectedOptionId) {
        try {
            return answerService.isAnswerCorrect(selectedOptionId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during isAnswerCorrect: " + e.getMessage());
            return false;
        }
    }

}
