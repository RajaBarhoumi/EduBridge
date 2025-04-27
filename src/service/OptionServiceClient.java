package service;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import models.Option;
import models.Question;
import rmi.OptionRemoteService;

public class OptionServiceClient {
    private OptionRemoteService optionService;

    public OptionServiceClient() {
        try {
            optionService = (OptionRemoteService) Naming.lookup("rmi://localhost:1099/OptionService");
        } catch (Exception e) {
            System.err.println("Error connecting to OptionRemoteService: " + e.getMessage());
        }
    }

    public boolean addOption(Option option) {
        try {
            optionService.addOption(option);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addOption: " + e.getMessage());
            return false;
        }
    }

    public List<Option> getOptionsByQuestion(int questionId) {
        try {
            return optionService.getOptionsByQuestion(questionId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getOptionsByQuestion: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteOption(int id) {
        try {
            optionService.deleteOption(id);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteOption: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOption(Option option) {
        try {
            optionService.updateOption(option);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateOption: " + e.getMessage());
            return false;
        }
    }
}
