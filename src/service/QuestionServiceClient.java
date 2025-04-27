package service;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import models.Question;
import rmi.QuestionRemoteService;

public class QuestionServiceClient {
    private QuestionRemoteService questionService;

    public QuestionServiceClient() {
        try {
            questionService = (QuestionRemoteService) Naming.lookup("rmi://localhost:1099/QuestionService");
        } catch (Exception e) {
            System.err.println("Error connecting to QuestionRemoteService: " + e.getMessage());
        }
    }

    public boolean addQuestion(Question question) {
        try {
            questionService.addQuestion(question);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addQuestion: " + e.getMessage());
            return false;
        }
    }

    public List<Question> getQuestionsByTest(int testId) {
        try {
            return questionService.getQuestionsByTest(testId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getQuestionsByTest: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteQuestion(int id) {
        try {
            questionService.deleteQuestion(id);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteQuestion: " + e.getMessage());
            return false;
        }
    }

    public boolean updateQuestion(Question question) {
        try {
            questionService.updateQuestion(question);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateQuestion: " + e.getMessage());
            return false;
        }
    }


}
