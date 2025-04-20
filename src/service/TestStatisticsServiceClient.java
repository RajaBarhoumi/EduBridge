package service;

import models.TestStatistics;
import rmi.TestStatisticsRemoteService;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class TestStatisticsServiceClient {
    private TestStatisticsRemoteService testStatisticsService;

    public TestStatisticsServiceClient() {
        try {
            testStatisticsService = (TestStatisticsRemoteService) Naming.lookup("rmi://localhost:1099/TestStatisticsService");
        } catch (Exception e) {
            System.err.println("Error connecting to TestStatisticsRemoteService: " + e.getMessage());
        }
    }

    public boolean addTestStatistics(TestStatistics stats) {
        try {
            testStatisticsService.addTestStatistics(stats);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addTestStatistics: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTestStatistics(TestStatistics stats) {
        try {
            testStatisticsService.updateTestStatistics(stats);
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateTestStatistics: " + e.getMessage());
            return false;
        }
    }

    public TestStatistics getTestStatisticsByTestId(int testId) {
        try {
            return testStatisticsService.getTestStatisticsByTestId(testId);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getTestStatisticsByTestId: " + e.getMessage());
            return null;
        }
    }
}
